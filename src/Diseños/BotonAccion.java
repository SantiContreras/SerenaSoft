package Diseños;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import rojeru_san.rsbutton.RSButtonRound;

public class BotonAccion extends RSButtonRound {

   public BotonAccion() {

    setPreferredSize(new Dimension(145, 42));

    setFont(new Font("Segoe UI", Font.BOLD, 14));

   

    setColorHover(new Color(20,90,180));

   

    setColorText(Color.WHITE);

    setColorTextHover(Color.WHITE);

    setCursor(new Cursor(Cursor.HAND_CURSOR));
}

  

}