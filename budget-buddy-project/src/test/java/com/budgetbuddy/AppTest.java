package com.budgetbuddy;

import javax.swing.*;
import java.awt.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class AppTest {
    private App app;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        // Mock the external dependencies
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Setup returns for the connection mock
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        // Instantiate the object under test
        app = new App() {
            @Override
            protected Connection getConnection() throws SQLException {
                return mockConnection; // Return the mocked connection here
            }
        };
    }

    @Test
    public void testSignInWithValidCredentials() throws SQLException {
        when(mockResultSet.next()).thenReturn(true); // Simulate found user

        
        app.signInUser();

        verify(mockStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
    }


    //These creds are valid, they exist in database. But when this method is executed, they show as invalid.
    @Test
    public void testSignInWithInvalidUsername() throws SQLException {
        // Arrange: Set the expected result set behavior
        JTextField usernameFieldMock = mock(JTextField.class);
        when(usernameFieldMock.getText()).thenReturn("sarah"); // Provide an existing username

        JPasswordField passwordFieldMock = mock(JPasswordField.class);
        when(passwordFieldMock.getPassword()).thenReturn("hello".toCharArray()); // Provide a wrong password

        app.setUsernameField(usernameFieldMock);
        app.setPasswordField(passwordFieldMock);

        app.signInUser();
        verify(app, times(1)).showErrorMessage("Incorrect username or password - Test.");
    }

    @Test
    public void testSignInWithWrongPassword() throws SQLException {
        // Arrange: Mock the behavior of the usernameField and passwordField
        JTextField usernameFieldMock = mock(JTextField.class);
        when(usernameFieldMock.getText()).thenReturn("sarah"); // Provide an existing username

        JPasswordField passwordFieldMock = mock(JPasswordField.class);
        when(passwordFieldMock.getPassword()).thenReturn("towson".toCharArray()); // Provide a wrong password

        app.setUsernameField(usernameFieldMock);
        app.setPasswordField(passwordFieldMock);

        app.signInUser();
}


    @Test
    public void testSignInWithEmptyUsername() throws SQLException {
        // Arrange: Mock the behavior of the usernameField to return an empty string
        JTextField usernameFieldMock = mock(JTextField.class);
        when(usernameFieldMock.getText()).thenReturn("");

        // Set the mocked usernameField in your app
        app.setUsernameField(usernameFieldMock);

        app.signInUser();
    }

    @Test
    public void testSignInWithEmptyPassword() throws SQLException {
        // Arrange: Mock the behavior of the usernameField to return an empty string
        JPasswordField passwordFieldMock = mock(JPasswordField.class);
        when(passwordFieldMock.getPassword()).thenReturn(new char[0]);

        // Set the mocked usernameField in your app
        app.setPasswordField(passwordFieldMock);

        app.signInUser();
    }
}
