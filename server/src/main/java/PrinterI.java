import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PrinterI implements Demo.Printer {

	@Override
	public String printString(String s, com.zeroc.Ice.Current current) {
		String[] parts = s.split(":", 3);
		String clientUsername = parts[0];
		String clientHostname = parts[1];
		String message = parts[2];

		StringBuilder response = new StringBuilder();

		if (isPositiveInteger(message)) {
			int num = Integer.parseInt(message);
			System.out.println(clientUsername + ":" + clientHostname + " sent a positive integer. Calculating prime factors...");
			java.util.List<Integer> factors = primeFactors(num);

			StringBuilder factorsStr = new StringBuilder();
			for (int factor : factors) {
				factorsStr.append(factor).append(", ");
			}

			// Remove the last comma and space
			if (factorsStr.length() > 0) {
				factorsStr.setLength(factorsStr.length() - 2);
			}
			response.append("Prime factors of ").append(num).append(": ").append(factorsStr);
		} else if (message.startsWith("listifs")) {
			System.out.println(clientUsername + ":" + clientHostname + " requested logical interfaces.");
			String interfaces = listInterfaces();
			response.append("Logical interfaces: \n").append(interfaces);
		} else if (message.startsWith("listports ")) {
			String ipAddress = message.substring("listports ".length()).trim();
			if (isValidIPAddress(ipAddress)) {
				System.out.println(clientUsername + ":" + clientHostname + " requested open ports for IP: " + ipAddress);
				String portsInfo = listOpenPorts(ipAddress);
				response.append("Open ports for IP ").append(ipAddress).append(": \n").append(portsInfo);
			} else {
				response.append("Invalid IP address.");
			}
		}else if (message.startsWith("!")) {
			String command = message.substring(1).trim(); // Remueve el "!" y extrae el comando
			System.out.println(clientUsername + ":" + clientHostname + " requested execution of command: " + command);
			String commandOutput = executeSystemCommand(command);
			response.append(commandOutput);
		}else {
			response.append("Message from ").append(clientUsername).append(":").append(clientHostname).append(": ").append(message);
		}

		System.out.println(response.toString());

		return response.toString();
	}

	private boolean isPositiveInteger(String s) {
		try {
			int num = Integer.parseInt(s);
			return num > 0;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private java.util.List<Integer> primeFactors(int n) {
		java.util.List<Integer> factors = new java.util.ArrayList<>();
		for (int i = 2; i <= n / i; i++) {
			while (n % i == 0) {
				factors.add(i);
				n /= i;
			}
		}
		if (n > 1) {
			factors.add(n);
		}
		return factors;
	}

	private String listInterfaces() {
		StringBuilder output = new StringBuilder();
		String line;
		try {
			Process p;

			// Detectar el sistema operativo para ejecutar el comando adecuado
			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				p = Runtime.getRuntime().exec("ipconfig");
			} else {
				p = Runtime.getRuntime().exec("ifconfig");
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));


			while ((line = br.readLine()) != null) {
				output.append(line).append("\n");
			}
			br.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error fetching logical interfaces.";
		}
		return output.toString();
	}


	private boolean isValidIPAddress(String ip) {
		String regex = "^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."
				+ "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."
				+ "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\."
				+ "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
		return ip.matches(regex);
	}


	private String listOpenPorts(String ipAddress) {
		StringBuilder output = new StringBuilder();
		String line;
		try {
			Process p = Runtime.getRuntime().exec("nmap -Pn " + ipAddress);

			// Leer la salida estándar de nmap (la lista de puertos abiertos)
			BufferedReader stdBr = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
			while ((line = stdBr.readLine()) != null) {
				output.append(line).append("\n");
			}
			stdBr.close();

			// Leer la salida de error de nmap
			BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
			while ((line = errorBr.readLine()) != null) {
				System.out.println("Error: " + line); // Imprime la salida de error para diagnóstico
			}
			errorBr.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error fetching open ports.";
		}
		return output.toString();
	}

	private String executeSystemCommand(String command) {
		StringBuilder output = new StringBuilder();
		String line;
		try {
			Process p = Runtime.getRuntime().exec(command);

			// Leer la salida estándar del comando
			BufferedReader stdBr = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
			while ((line = stdBr.readLine()) != null) {
				output.append(line).append("\n");
			}
			stdBr.close();

			// Leer la salida de error del comando
			BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
			while ((line = errorBr.readLine()) != null) {
				output.append("Error: ").append(line).append("\n"); // Agrega salida de error al output
			}
			errorBr.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error executing system command.";
		}
		return output.toString();
	}



}

