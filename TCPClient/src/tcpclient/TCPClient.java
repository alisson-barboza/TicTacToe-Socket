/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpclient;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import tictactoe.Tictactoe;

public class TCPClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ObjectInputStream objIn;        
        ObjectOutputStream objOut;
        Socket s = null;
        Tictactoe jogoDaVelha = new Tictactoe();
        boolean playFirst = false;
        Scanner entrada = new Scanner(System.in);
        char jogador = 0;
        try {
            int port = 6868;
            InetAddress server = InetAddress.getLocalHost();
            s = new Socket(server, port);
            objOut = new ObjectOutputStream(s.getOutputStream());
            objOut.flush();
            objIn = new ObjectInputStream(s.getInputStream());

            //==Game
            int randomNumber = (int) ((Math.random() * 10)%2);
            
            if(randomNumber == 1) {
            	objOut.writeBoolean(false);
            	objOut.flush();
            	playFirst = true;
            	System.out.println("Parab�ns, voc� joga primeiro");
            	do {
            		System.out.println("Escolha X || O");
            		jogador = entrada.nextLine().toUpperCase().charAt(0);
            	}while(jogador != 'X' && jogador != 'O');
            	objOut.writeChar(jogador);
            	objOut.flush();
            }else {
            	objOut.writeBoolean(true);
            	objOut.flush();
            	playFirst = false;
            	System.out.println("Aguade enquanto o advers�rio escolhe com quem ir� jogar");
            	if(objIn.readChar() == 'X') {
            		jogador = 'O';
            	}else {
            		jogador = 'X';
            	}
            	System.out.println(jogador);
            }
            //Come�o do jogo
            if(playFirst) {
            	do {
            		jogoDaVelha.printMatrix();
            		System.out.println("Fa�a sua jogada:");
            		jogoDaVelha.makeaPlay(jogador);
            		objOut.writeObject(jogoDaVelha);
            		objOut.flush();
            		if(jogoDaVelha.hasWinner()) {
            			System.out.println("Parab�ns voc� venceu!!");
            			break;
            		}else {
            			System.out.println("Aguarde enquanto o advers�rio est� fazendo a jogada...");
            		}
            		jogoDaVelha = (Tictactoe) objIn.readObject();
            		if(jogoDaVelha.hasWinner()) {
            			System.out.println("O avers�rio venceu!");
            		}
            	}while(!jogoDaVelha.hasWinner());
            }else {
            	do {
            		jogoDaVelha = (Tictactoe) objIn.readObject();
            		if(jogoDaVelha.hasWinner()) {
            			jogoDaVelha.printMatrix();
            			System.out.println("O advers�rio venceu!");
            			break;
            		}
            		jogoDaVelha.printMatrix();
            		System.out.println("Fa�a sua jogada:");
            		jogoDaVelha.makeaPlay(jogador);
            		objOut.writeObject(jogoDaVelha);
            		objOut.flush();
            		if(jogoDaVelha.hasWinner()) {
            			System.out.println("Parab�ns voc� venceu!!");
            		}
            	}while(!jogoDaVelha.hasWinner());
            }
            
            jogoDaVelha.printMatrix();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {if (s != null )
                try {
                    s.close();
                } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }
    
}