import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.*;

public class Program {
	
	public static void main(String[] args) {
		System.out.println("Now we are running a real program!!!");
		new DummyRedirect(Program::good_proc).actionPerformed(null);
		
		JFrame window = new JFrame();
		window.setTitle("Java is dumb, does not have fn");
		JMenu file_bar = new JMenu("Fula file");
		JMenuBar mb = new JMenuBar();
		mb.add(file_bar);
		window.setJMenuBar(mb);
		window.setVisible(true);
	}
	public static void good_proc(Object b)
	{
		System.out.println("Now its happening.");
	}
}

class DummyRedirect implements ActionListener {
	Consumer fn;
	public DummyRedirect(Consumer fn)
	{
		this.fn = fn;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		fn.accept(null);
	}
	
}
