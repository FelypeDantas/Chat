package Redes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatCliente extends JFrame {
	
	JTextField textoParaEnviar;
	PrintWriter escritor;
	Socket socket;
	String nome;
	JTextArea textoRecebido;
	Scanner leitor;
	
	private class EscutaServidor implements Runnable{

		@Override
		public void run() {
			try {
				String texto;
				while((texto = leitor.nextLine()) != null) {
					textoRecebido.append(texto + "\n");
				}
			}catch(Exception x) {}
			
			
		}
		
	}
	
	public ChatCliente(String nome) {
		super("Chat: " + nome);
		this.nome = nome;
		
		Font fonte = new Font("Serif", Font.PLAIN, 26);
		textoParaEnviar = new JTextField();
		textoParaEnviar.setFont(fonte);
		JButton botao = new JButton("Enviar");
		botao.setFont(fonte);
		botao.addActionListener(new EnviarListener());
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, textoParaEnviar);
		envio.add(BorderLayout.EAST, botao);
		
		textoRecebido = new JTextArea();
		textoRecebido.setFont(fonte);
		JScrollPane scroll = new JScrollPane(textoRecebido);
		
		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, envio);
		
		configurarRede();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}
	
	private class EnviarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			escritor.println(nome + " : " + textoParaEnviar.getText());
			escritor.flush();
			textoParaEnviar.setText("");
			textoParaEnviar.requestFocus();
		}
		
	}
	
	private void configurarRede() {
		try {
		    socket = new Socket("127.0.0.1", 5000);
		    escritor = new PrintWriter(socket.getOutputStream());
		    leitor = new Scanner(socket.getInputStream());
		    new Thread(new EscutaServidor()).start();
		} catch(Exception e){}
	}
	
   public static void main(String args[]) {
	   new ChatCliente("Felype");
	   new ChatCliente("Byanca");
   }
}
