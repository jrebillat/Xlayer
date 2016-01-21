package net.alantea.xlayer.test.swing;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.awt.Component;
import java.util.HashMap;

/**
 * This example, like all Swing examples, exists in a package:
 * in this case, the "start" package.
 * If you are using an IDE, such as NetBeans, this should work 
 * seamlessly.  If you are compiling and running the examples
 * from the command-line, this may be confusing if you aren't
 * used to using named packages.  In most cases,
 * the quick and dirty solution is to delete or comment out
 * the "package" line from all the source files and the code
 * should work as expected.  For an explanation of how to
 * use the Swing examples as-is from the command line, see
 * http://docs.oracle.com/javase/javatutorials/tutorial/uiswing/start/compile.html#package
 */


/*
 * HelloWorldSwing.java requires no other files. 
 */
import javax.swing.JFrame;

import net.alantea.xlayer.Manager;

public class HelloWorldSwing2 {
   static final String DONNEES_XML = "<?xml version=\"1.0\"?>\n"
         + "<jPanel>\n"
         + "<jLabel>\n"
         + "<text>Hello World too...</text>\n"
         + "</jLabel>\n"
         + "<jButton name='mybutton1'>\n"
         + "<text>Hello World too...</text>\n"
         + "<addActionListener><ScriptedActionListener><script>print('hello world');</script></ScriptedActionListener></addActionListener>\n"
         + "</jButton>\n"
         + "</jPanel>\n";
   
   private static HashMap<String, Component> componentMap;
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setTitle("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Manager.addPackage("javax.swing");
        // Manager.addClass("javax.swing.JLabel");
       // Manager.addClass("javax.swing.JButton");
      //  Manager.addClass("javax.swing.JPanel");
        Manager.parse(frame.getContentPane(), DONNEES_XML);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private static void createComponentMap(JFrame frame) {
       componentMap = new HashMap<String,Component>();
       Component[] components = frame.getContentPane().getComponents();
       for (int i=0; i < components.length; i++) {
          componentMap.put(components[i].getName(), components[i]);
       }
    }

    public static Component getComponentByName(String name) {
       if (componentMap.containsKey(name)) {
          return (Component) componentMap.get(name);
       }
       else return null;
    }
}