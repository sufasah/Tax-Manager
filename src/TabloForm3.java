import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI.ListSelectionHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.TableView.TableCell;
import javax.xml.bind.Marshaller.Listener;
import javax.xml.crypto.dsig.keyinfo.PGPData;

import java.lang.*;
import java.math.BigDecimal;
import java.nio.channels.SelectableChannel;
import java.security.KeyStore.Entry;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
public class TabloForm3 extends JFrame{
	TabloForm3(String faturaTur,String yil){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		Toolkit t= Toolkit.getDefaultToolkit();
		setResizable(false);
		setSize(t.getScreenSize().width*3/5,t.getScreenSize().height*2/4);
		setLocation(t.getScreenSize().width/2-getWidth()/2,t.getScreenSize().height/2-getHeight()*3/5);
		JLabel[] yazilar = new JLabel[7];
		for(JLabel l : yazilar)
			l=new JLabel();
		
		//yilda kaç fatura geldi toplam ne kadar tuttu  toplam ödeme adet miktar kalan ödeme
		//tek tek aylarda kaç fatura toplam nekadar toplam yapýlan ödeme kalan ödeme
		setVisible(true);
	}
	
}