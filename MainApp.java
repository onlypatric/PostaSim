import java.awt.Dimension;

import javax.swing.Box.Filler;
import javax.swing.JOptionPane;

import Comps.*;

/**
 * MainApp, this is the highest level of the application
 * @see Comps.Posta
 * @see Comps.Postino
 * 
 * @author Pintescul Patric
 * @version 1.0.2
 */
public class MainApp {
    public static final int 
        DEFAULT_CICLI_POSTINI = 5,
        DEFAULT_CICLI_LETTORI = 10;
    public static void main(String[] args) {
        Postino[] postini = new Postino[10];
        Lettore[] lettori = new Lettore[3];

        Posta p1=new Posta("Casa n1"), p2=new Posta("Casa n2"), p3=new Posta("Casa n3");
        Listed<String> s = new Listed<String>();
        Window win = new Window(
            new Heading("Case"),

            // COMPONENTS TO BE EDITED
            new Horizontal(
                new Filler(new Dimension(0,0), new Dimension(0,0),
                          new Dimension(Short.MAX_VALUE, 0)),
                p1,
                new Filler(new Dimension(0,0), new Dimension(0,0),
                          new Dimension(Short.MAX_VALUE, 0)),
                p2,
                new Filler(new Dimension(0,0), new Dimension(0,0),
                          new Dimension(Short.MAX_VALUE, 0)),
                p3,
                new Filler(new Dimension(0,0), new Dimension(0,0),
                          new Dimension(Short.MAX_VALUE, 0))
            ).padding(20),

            // BUTTON & LISTENER
            new Horizontal(
                new Button("Start simulation").action(e->{
                    // Action listener for the button
                    new Thread(() -> {
                        // Clearing the log list
                        s.clear();
                        // Creating Postino objects
                        for (int i = 0; i < postini.length; i++) {
                            postini[i] = new Postino("Postino " + i, s).setCiclo(DEFAULT_CICLI_POSTINI);
                        }
                        // Creating Lettore objects
                        for (int i = 0; i < lettori.length; i++) {
                            lettori[i] = new Lettore("Lettore " + i, s).setCiclo(DEFAULT_CICLI_LETTORI);
                        }
                        ;
                        // Linking Posta objects with Postino objects
                        p1.link(postini);
                        p2.link(postini);
                        p3.link(postini);
                        p1.link(lettori);
                        p2.link(lettori);
                        p3.link(lettori);
                        // Starting Postino threads
                        for (Postino postino : postini) {
                            postino.start();
                        }
                        // Starting Lettore threads
                        for (Lettore lettore : lettori) {
                            lettore.start();
                        }
                        // Waiting for Postino threads to finish
                        for (Postino postino : postini) {
                            try {
                                postino.join();
                            } catch (InterruptedException e1) {
                                // Exception handling
                            }
                        }
                        // Waiting for Lettore threads to finish
                        for (Lettore lettore : lettori) {
                            try {
                                lettore.join();
                            } catch (InterruptedException e1) {
                                // Exception handling
                            }
                        }
                        // Showing end dialog
                        showEndDialog();
                    }).start();
                })
            ).padding(15),

            // DISPLAY RESULTS PART (LOGS)
            new Horizontal(
                new Vertical(
                    new Heading("Logs",Heading.Type.H2),
                    new ScrollArea(
                        s
                    )
                ).alignmentX(Vertical.AlignmentGuide.LEFT)
            )
        ).size(780,700).pos(144,144);
        win.setVisible(true);
    }

    /**
     * Will invoke a message dialog displaying the end of the simulation
     */
    public static void showEndDialog(){
        JOptionPane.showMessageDialog(null, "Simulation ended", "End", JOptionPane.INFORMATION_MESSAGE);
    }
}