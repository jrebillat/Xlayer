/**
 * 
 */
package net.alantea.xlayer.test.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author m021200
 *
 */
public class ScriptedActionListener implements ActionListener
{

   private static ScriptEngineManager manager = new ScriptEngineManager();
   private static ScriptEngine engine = manager.getEngineByName("JavaScript");
   
   private String script;
   
   public ScriptedActionListener()
   {
   }
   
   public void setScript(String script)
   {
      this.script = script;
   }

   /* (non-Javadoc)
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    */
   @Override
   public void actionPerformed(ActionEvent arg0)
   {
      try
      {
         engine.eval(script);
      }
      catch (ScriptException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
