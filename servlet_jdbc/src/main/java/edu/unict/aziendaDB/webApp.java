package edu.unict.aziendaDB;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/aziendaDB")

public class webApp extends HttpServlet {
    Connection connection ;
    final static String CONNECTION = "jdbc:mysql://localhost:3306/AziendaDB?user=root&password=Ciccio02?";

    
    @Override
    public void init()
    {
        try {
            connection = DriverManager.getConnection(CONNECTION);
        } catch (SQLException e) {
            System.out.println("Error while connecting to database");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

    }


    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><br>");
        out.println("<body><h1><center>AZIENDA DB</center></h1>");
        out.println("<h2>Lista Dipendenti</h2>");

        String query = "SELECT * FROM Dipendenti";

       try( Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(query))
        {
            while(result.next())
            {
                out.println("<br>");
                out.println("Nome : "+ result.getString("Nome"));
                out.println("Cognome : "+ result.getString("Cognome"));
                out.println("Ruolo : "+ result.getString("Ruolo"));
                out.println("Stipendio : "+ result.getString("Stipendio"));


            }

        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        
        out.println("<h3>Aggiungi un nuovo dipendente</h3>");
        out.println("<form action = '/aziendaDB' method = 'post'>");
        out.println("Nome : <input type = 'text' name='Nome' required> <br> <br>");
        out.println("Cognome : <input type = 'text' name='Cognome' required> <br> <br>");
        out.println("Ruolo : <input type = 'text' name='Ruolo' required> <br> <br>");
        out.println("Stipendio : <input type = 'number' name='Stipendio' required> <br> <br>");
        out.println("<input type = 'submit' value = 'Invia' />");

        out.println("</form>");
    

        out.println("</body></html>");
    }







    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String nome = request.getParameter("Nome");
        String cognome = request.getParameter("Cognome");
        String ruolo = request.getParameter("Ruolo");
        int stipendio;
        try {
        stipendio = Integer.parseInt(request.getParameter("Stipendio"));
        }catch(NumberFormatException e )
        {
            return;
        }
        String query = "INSERT INTO Dipendenti (Nome,Cognome,Ruolo,Stipendio) VALUES (?,?,?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, ruolo);
            stmt.setInt(4, stipendio);
            int rows = stmt.executeUpdate();
            out.write("AGGIUNTO RIGA" + rows);
        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }

        out.write("<br> <a href='/aziendaDB'> TORNA ALLA HOME </a>");
    }
}

