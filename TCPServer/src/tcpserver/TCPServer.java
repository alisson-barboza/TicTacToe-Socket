/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import tictactoe.Tictactoe;

/**
 *
 * @author almirpires
 */
public class TCPServer {
    static Socket client = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket slisten = new ServerSocket(6868);
            while (true){
                System.out.println("Aguardando Conexao.");
                client = slisten.accept();
                Connection conexao = new Connection(client);
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static class Connection extends Thread {
    	ObjectInputStream in;
        ObjectOutputStream out;
        Socket client;
        public Connection(Socket client){
            this.client = client;
            try {
                this.in = new ObjectInputStream(client.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.out = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.start();
        }
        @Override
        public void run(){
            String mensagem;
            boolean playFirst = false;
            char jogador = 0;
            Scanner entrada = new Scanner(System.in);
            Tictactoe jogoDaVelha = new Tictactoe();
            try {
            	
            	if(in.readBoolean()) {
                	playFirst = true;
                	System.out.println("Parabéns, você joga primeiro");
                	do {
                		System.out.println("Escolha X || O");
                		jogador = entrada.nextLine().toUpperCase().charAt(0);
                	}while(jogador != 'X' && jogador != 'O');
                	out.writeChar(jogador);
                	out.flush();
                }else {
                	playFirst = false;
                	System.out.println("Aguade enquanto o adversário escolhe com quem irá jogar");
                	if(in.readChar() == 'X') {
                		jogador = 'O';
                	}else {
                		jogador = 'X';
                	}
                	System.out.println("Você vai jogar com: " + jogador);
                }
            	//game starting
            	if(playFirst) {
                	do {
                		jogoDaVelha.printMatrix();
                		System.out.println("Faça sua jogada:");
                		jogoDaVelha.makeaPlay(jogador);
                		out.writeObject(jogoDaVelha);
                		out.flush();
                		if(jogoDaVelha.hasWinner()) {
                			System.out.println("Parabéns você venceu!!");
                			break;
                		}else {
                			System.out.println("Aguarde enquanto o adversário está fazendo a jogada...");
                		}
                		jogoDaVelha = (Tictactoe) in.readObject();
                		if(jogoDaVelha.hasWinner()) {
                			System.out.println("O adversário venceu!");
                		}
                	}while(!jogoDaVelha.hasWinner());
                }else {
                	do {
                		jogoDaVelha = (Tictactoe) in.readObject();
                		if(jogoDaVelha.hasWinner()) {
                			jogoDaVelha.printMatrix();
                			System.out.println("O adversário venceu!");
                			break;
                		}
                		jogoDaVelha.printMatrix();
                		System.out.println("Faça sua jogada:");
                		jogoDaVelha.makeaPlay(jogador);
                		out.writeObject(jogoDaVelha);
                		out.flush();
                		if(jogoDaVelha.hasWinner()) {
                			System.out.println("Parabéns você venceu!!");
                		}
                	}while(!jogoDaVelha.hasWinner());
                }
            	jogoDaVelha.printMatrix();
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
    }
    
}