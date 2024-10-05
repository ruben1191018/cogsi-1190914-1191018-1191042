package basic_demo;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientTest {

    private ChatClient chatClient;
    private Socket socketMock;
    private Scanner scannerMock;
    private PrintWriter writerMock;

    @Before
    public void setUp() throws Exception {
        socketMock = mock(Socket.class);
        scannerMock = mock(Scanner.class);
        writerMock = mock(PrintWriter.class);

        chatClient = spy(new ChatClient("localhost", 12345));

        doReturn(scannerMock).when(socketMock).getInputStream();
        doReturn(writerMock).when(socketMock).getOutputStream();

        doReturn("TestUser").when(chatClient).getName();
    }

    @Test
    public void testNameSubmission() {
        when(scannerMock.nextLine()).thenReturn("SUBMITNAME", "NAMEACCEPTED TestUser", "MESSAGE Hello World!");

        Thread clientThread = new Thread(chatClient);
        clientThread.start();

        verify(writerMock, times(1)).println("TestUser");

        assertEquals("Chatter - TestUser", chatClient.frame.getTitle());

        assertTrue(chatClient.messageArea.getText().contains("Hello World!"));

        clientThread.interrupt();
    }

    @Test
    public void testMessageSending() {
        chatClient.textField.setText("Hello!");

        chatClient.textField.postActionEvent();

        verify(writerMock, times(1)).println("Hello!");

        assertEquals("", chatClient.textField.getText());
    }
}
