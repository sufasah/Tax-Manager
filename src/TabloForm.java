import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicListUI.ListSelectionHandler;
import javax.swing.plaf.basic.BasicTreeUI.SelectionModelPropertyChangeHandler;
import javax.swing.table.AbstractTableModel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
public class TabloForm extends JFrame{
	HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>> faturaList;
	protected FaturaFileHandler ffh=new FaturaFileHandler();
	int gelenFaturaAdet;
	JScrollPane sp;
	JTable tbl;
	JLabel l1,l2,l3,l4;
	JTextField tf1,tf2,tf3,tf4,tf5,tf6;
	JButton b0,b1;
	
	private void tumFaturaAl(DefaultTableModel mdl) {
		mdl.setRowCount(gelenFaturaAdet);
		Set Turler =faturaList.keySet();
		Iterator<String> itTur=Turler.iterator();
		int nextRow=0;
		while(itTur.hasNext()) {
			String Tur=itTur.next();
			Set Yillar = faturaList.get(Tur).keySet();
			Iterator<String> itYil=Yillar.iterator();
			while(itYil.hasNext()) {
				String Yil=itYil.next();
				Set Aylar = faturaList.get(Tur).get(Yil).keySet();
				Iterator<String> itAy=Aylar.iterator();
				while(itAy.hasNext()) {
					String Ay=itAy.next();
					Set Gunler = faturaList.get(Tur).get(Yil).get(Ay).keySet();
					Iterator<String> itGun=Gunler.iterator();
					while(itGun.hasNext()) {
						String Gun = itGun.next();
						String Miktar =faturaList.get(Tur).get(Yil).get(Ay).get(Gun);
						tbl.setValueAt(Tur, nextRow, 0);
						tbl.setValueAt(Gun+"."+Ay+"."+Yil, nextRow, 1);
						tbl.setValueAt(Miktar, nextRow, 2);
						nextRow++;
					}
				}
			}
		}
	}
	
	TabloForm(){
		String path= System.getProperty("user.dir")+"\\faturalar\\";
		gelenFaturaAdet=0;
		faturaList = new HashMap<String,HashMap<String,HashMap<String,HashMap<String,String>>>>() ;
		try {
			BufferedInputStream itur = new BufferedInputStream(new FileInputStream(new File(path+"turler.txt")));
			StringBuilder turReaden = new StringBuilder();
			while(FaturaFileHandler.satirAl(itur, turReaden)) {
				String gelenTur= turReaden.toString();
				faturaList.put(gelenTur, new HashMap<String,HashMap<String,HashMap<String,String>>>());
				BufferedInputStream inext = new BufferedInputStream(new FileInputStream(new File(path+turReaden+".txt")));
				StringBuilder nextReaden= new StringBuilder();
				while(FaturaFileHandler.satirAl(inext, nextReaden)) {
					String gelenTarih= nextReaden.toString();
					String gelenYil=gelenTarih.substring(6,10);
					String gelenAy = gelenTarih.substring(3,5);
					String gelenGun = gelenTarih.substring(0,2);
					int j=11;
					while(j<gelenTarih.length()&&gelenTarih.charAt(j)!=' ')
						j++;
					if(!faturaList.get(gelenTur).containsKey(gelenYil))
						faturaList.get(gelenTur).put(gelenYil  ,new HashMap<String,HashMap<String,String>>());
					if(!faturaList.get(gelenTur).get(gelenYil).containsKey(gelenAy))
						faturaList.get(gelenTur).get(gelenYil).put(gelenAy,new HashMap<String,String>());
					if(!faturaList.get(gelenTur).get(gelenYil).get(gelenAy).containsKey(gelenGun))
						faturaList.get(gelenTur).get(gelenYil).get(gelenAy).put(gelenGun,gelenTarih.substring(11,j));
					gelenFaturaAdet++;
				}
				inext.close();
			}
			itur.close();
			
		}catch(IOException exc) {}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		Toolkit t= Toolkit.getDefaultToolkit();
		setResizable(false);
		setSize(t.getScreenSize().width*4/5,t.getScreenSize().height*3/4);
		setLocation(t.getScreenSize().width/2-getWidth()/2,t.getScreenSize().height/2-getHeight()*3/5);
		
		int aralikx=getWidth()/20;
		int araliky=getHeight()/30;
		
		JPopupMenu pum= new JPopupMenu();
		JMenuItem i0= new JMenuItem("Seçili Verileri Sil");
		JMenuItem i1= new JMenuItem("Faturaya Yapýlan Ödemeleri Listele");
		JMenuItem i2= new JMenuItem("Yýl Bazýnda Bilgi Tablosu Çýkart");

		MyPumiActionListener mpumial=new MyPumiActionListener();
		i0.setName("i0");
		i1.setName("i1");
		i2.setName("i2");

		pum.add(i0);
		pum.add(i1);
		pum.add(i2);

		
		
		l1 = new JLabel("Tür: ",JLabel.RIGHT);
		l1.setBounds(aralikx,araliky,getWidth()/25,getHeight()/24);
		tf1 = new JTextField();
		tf1.setBounds(l1.getX()+l1.getWidth()+5, araliky, getWidth()/10, getHeight()/20);
		
		l2 = new JLabel("Tarih: ",JLabel.RIGHT);
		l2.setBounds(tf1.getX()+tf1.getWidth()+aralikx, araliky,l1.getWidth(),l1.getHeight());
		tf2 = new JTextField();
		tf2.setBounds(l2.getX()+l2.getWidth()+5, araliky, tf1.getWidth(), tf1.getHeight());
		tf2.setText(new SimpleDateFormat("dd.MM.YYYY").format(new Date()).substring(0,10));
		
		l3 = new JLabel("Miktar: ",JLabel.RIGHT);
		l3.setBounds(tf2.getX()+tf2.getWidth()+aralikx, araliky,l1.getWidth(),l1.getHeight());
		tf3 = new JTextField();
		tf3.setBounds(l3.getX()+l3.getWidth()+5, araliky, tf1.getWidth(), tf1.getHeight());
		
		b0 = new JButton("Ekle");
		b0.setBounds(tf3.getX()+tf3.getWidth()+aralikx,araliky,getWidth()/15,tf1.getHeight());
		
		tf4 = new JTextField();
		tf4.setBounds(tf1.getX(), araliky*2+tf1.getHeight(), tf1.getWidth(), tf1.getHeight());
		b1 = new JButton("Ara");
		b1.setBounds(tf4.getX()+tf4.getWidth()+5,tf4.getY(),b0.getWidth(),tf4.getHeight());
		
		
		DefaultTableModel mdl = new DefaultTableModel(gelenFaturaAdet,3);
		
		tbl = new JTable(mdl);
		tbl.setColumnSelectionAllowed(false);
		for(MouseListener ml : tbl.getMouseListeners())
			tbl.removeMouseListener(ml);
		for(KeyListener ml : tbl.getKeyListeners())
			tbl.removeKeyListener(ml);
		for(MouseMotionListener ml : tbl.getMouseMotionListeners())
			tbl.removeMouseMotionListener(ml);
		tbl.setRowHeight(30);
		tbl.getTableHeader().setReorderingAllowed(false);
		
		TableColumn c0 = tbl.getColumnModel().getColumn(0);
		TableColumn c1=tbl.getColumnModel().getColumn(1);
		TableColumn c2= tbl.getColumnModel().getColumn(2);
		
		JTextField dce0comp = new JTextField();
		JTextField dce1comp = new JTextField();
		JTextField dce2comp = new JTextField();
		DefaultCellEditor dce0 = new DefaultCellEditor(dce0comp);
		DefaultCellEditor dce1 = new DefaultCellEditor(dce1comp);
		DefaultCellEditor dce2 = new DefaultCellEditor(dce2comp);
		
		c0.setCellEditor(dce0);
		c1.setCellEditor(dce1);
		c2.setCellEditor(dce2);
		
		c0.setHeaderValue("Fatura Türü");
		c1.setHeaderValue("Fatura Tarihi");
		c2.setHeaderValue("Fatura Miktarý");
		DefaultTableCellRenderer centerRenderer= new DefaultTableCellRenderer() ;
		centerRenderer.setHorizontalAlignment(centerRenderer.CENTER);
		c0.setCellRenderer(centerRenderer);
		c1.setCellRenderer(centerRenderer);
		c2.setCellRenderer(centerRenderer);
		
		sp=new JScrollPane(tbl);
		sp.setLocation(0,getHeight()/5);
		sp.setSize(getWidth()*197/200,getHeight()-getHeight()*27/100);
		
		tumFaturaAl(mdl);
		
		ActionListener deleteSelectedRows = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(TabloForm.this, "Fatura Verilerini Kalýcý Olarak Silmek Ýstediðinize Eminmisiniz ?", "Fatura Sil",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
				{
					int[] selectedRows = tbl.getSelectedRows();
					for(int i=0;i<selectedRows.length;i++) {
						String Tur = (String)tbl.getValueAt(selectedRows[i], 0);
						String Tarih=(String)tbl.getValueAt(selectedRows[i], 1);
						String Yil = Tarih.substring(6,10);
						String Ay = Tarih.substring(3,5);
						String Gun  = Tarih.substring(0,2);
						try {
							ffh.faturaSil(Tur,Tarih);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(TabloForm.this, e1.getStackTrace(),e1.getMessage(),JOptionPane.ERROR_MESSAGE);
						}
						faturaList.get(Tur).get(Yil).get(Ay).remove(Gun);
						if(faturaList.get(Tur).get(Yil).get(Ay).size()==0)
							faturaList.get(Tur).get(Yil).remove(Ay);
						if(faturaList.get(Tur).get(Yil).size()==0)
							faturaList.get(Tur).remove(Yil);
						if(faturaList.get(Tur).size()==0)
							faturaList.remove(Tur);
						gelenFaturaAdet--;
					}
					for(int i=0;i<selectedRows.length;i++)
						mdl.removeRow(selectedRows[i]-i);
				}
			}
		};
		
		MyCellEditorListener CEL = new MyCellEditorListener(this,tbl);
		
		dce0.addCellEditorListener(CEL);
		dce1.addCellEditorListener(CEL);
		dce2.addCellEditorListener(CEL);
		
		KeyAdapter turTextTypedAction = new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				JTextField tf=(JTextField)e.getSource();
				if(tf==tf1&&e.getKeyChar()==e.VK_ENTER)
					b0.doClick();
				char c=e.getKeyChar();
				int caretpos=tf.getCaretPosition();
				String text=tf.getText();
				if(!Character.isLetterOrDigit(c)|| text.length()>=20)
					e.consume();
			}
		};
		KeyAdapter tarihTextTypedAction = new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				JTextField tf = (JTextField)e.getSource();
				if(tf==tf2&&e.getKeyChar()==e.VK_ENTER)
					b0.doClick();
				char c=e.getKeyChar();
				int caretpos=tf.getCaretPosition();
				String text=tf.getText();
				if(Character.isDigit(c))
				{
					if(caretpos==10) {
						e.consume();
						return;
					}
					if(caretpos==2|| caretpos==5) 
						tf.setCaretPosition(caretpos++);
					switch(caretpos) {
					case 0:
						if(c>'3'||(c=='3'&&text.charAt(1)>'1')) {
							tf.setText("31"+text.substring(2));
							tf.setCaretPosition(1);
							e.consume();
							return;
						}
						if(c=='0'&&text.charAt(1)=='0') {
							tf.setText("01"+text.substring(2));
							tf.setCaretPosition(1);
							e.consume();
							return;
						} 
						break;
					case 1:
						if(text.charAt(0)=='0'&&c=='0'){
							tf.setText("01"+text.substring(2));
							tf.setCaretPosition(4);
							e.consume();
							return;
						}
						if(text.charAt(0)=='3'&&c>'1')
						{
							tf.setText("31"+text.substring(2));
							tf.setCaretPosition(4);
							e.consume();
							return;
						}
						break;
					case 3:
						if(c=='0' && text.charAt(4)=='0')
						{
							tf.setText(text.substring(0,3)+"01"+text.substring(5));
							tf.setCaretPosition(4);
							e.consume();
							return;
						}
						if(c>'1'||(c=='1'&&text.charAt(4)>'2'))
						{
							tf.setText(text.substring(0,3)+"12"+text.substring(5));
							tf.setCaretPosition(4);
							e.consume();
							return;
						}
						break;
					case 4:
						if(c=='0'&&text.charAt(3)=='0') {
							tf.setText(text.substring(0,3)+"01"+text.substring(5));
							tf.setCaretPosition(6);
							e.consume();
							return;
						}
						if(text.charAt(3)=='1'&&c>'2') 
						{
							tf.setText(text.substring(0,3)+"12"+text.substring(5));
							tf.setCaretPosition(6);
							e.consume();
							return;
						}
						break;
					case 6:
						if(c<'1') {
							c=1;
							e.setKeyChar('1');
						}
						if(c>'2') {
							c=2;
							e.setKeyChar('2');
						}
						break;
					}
					tf.select(caretpos,caretpos+1);
					tf.replaceSelection("");
				}
				else if((c=='\b'||c==127&&caretpos<10)) {
					if(caretpos==2||caretpos==5)
						e.setKeyChar('.');
					else if(caretpos==0&&text.charAt(2)=='.') {
						e.consume();
					}
					else
						e.setKeyChar('1');
					
				}
				else {
					e.consume();
				}
			}
		};
		KeyAdapter miktarTextTypedAction = new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				JTextField tf = (JTextField)e.getSource();
				if(tf==tf3&&e.getKeyChar()==e.VK_ENTER)
					b0.doClick();
				char c=e.getKeyChar();
				int caretpos = tf.getCaretPosition();
				String text=tf.getText();
				if((c=='\b'||c==127||Character.isDigit(c))) {
					int in=tf.getText().indexOf('.');
					if(in!=-1 && caretpos>in && text.length()-in==3)
						e.consume();
				}
				else if(c!='.'||caretpos<text.length()-2||text.contains(".")){
					e.consume();
				}
			}
		}; 
		AbstractAction bosAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				e.setSource("");
			}
		};
		AbstractAction hepsiniSec = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				tbl.setRowSelectionInterval(0, tbl.getRowCount()-1);
			}
		};
		AbstractAction stopCellEditing = new AbstractAction(){
			public void actionPerformed(ActionEvent e) {
				CEL.editingRow=tbl.getEditingRow();
				CEL.editingColumn=tbl.getEditingColumn();
				CEL.oldVal=(String)tbl.getValueAt(CEL.editingRow, CEL.editingColumn);
				if(JOptionPane.showConfirmDialog(TabloForm.this,"Fatura Verilerini Kalýcý Olarak Düzenlemek Ýstiyormusunuz ?","Fatura Düzenle",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
					tbl.getCellEditor().stopCellEditing();
				else
					tbl.getCellEditor().cancelCellEditing();
				tbl.requestFocus();
			};
		};

		dce0comp.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "stopCellEditing");
		dce1comp.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "stopCellEditing");
		dce2comp.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "stopCellEditing");
		dce0comp.getActionMap().put("stopCellEditing", stopCellEditing);
		dce1comp.getActionMap().put("stopCellEditing", stopCellEditing);
		dce2comp.getActionMap().put("stopCellEditing", stopCellEditing);
		
		dce0comp.addKeyListener(turTextTypedAction);
		dce1comp.addKeyListener(tarihTextTypedAction);
		dce2comp.addKeyListener(miktarTextTypedAction);
		
		tf1.addKeyListener(turTextTypedAction);		
		tf2.addKeyListener(tarihTextTypedAction);		
		tf3.addKeyListener(miktarTextTypedAction);
		
		tbl.setInputMap(tbl.WHEN_FOCUSED,new InputMap());
		tbl.setInputMap(tbl.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		tbl.setSurrendersFocusOnKeystroke(true);
		ActionMap myActionMap = new ActionMap();
		tbl.setActionMap(myActionMap);
		tbl.getInputMap(tbl.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"),"deleteSelectedRows");
		tbl.getInputMap(tbl.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl A"),"selectAllRows");
		tbl.getActionMap().put("selectAllRows",hepsiniSec);
		tbl.getActionMap().put("deleteSelectedRows", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedRows.actionPerformed(e);
			}
		});
		tbl.getInputMap(tbl.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("S"),"bos");
		tbl.getActionMap().put("bos", bosAction);
		
		tableRowDrag trd = new tableRowDrag(tbl,CEL);
		
		
		tbl.addMouseMotionListener(trd);
		tbl.addMouseListener(new java.awt.event.MouseAdapter() {
			int lastClicked=0;

			public void mouseClicked(MouseEvent e) {
				
				if(e.getButton()==e.BUTTON1) {
					tbl.requestFocus();
					if(e.isShiftDown()) {
						int rowIndex=tbl.rowAtPoint(e.getPoint());
						if(tbl.isRowSelected(rowIndex)) {
							tbl.removeRowSelectionInterval(lastClicked, rowIndex);
						}
						else {
							tbl.addRowSelectionInterval(lastClicked, rowIndex);
						}
						lastClicked=rowIndex;
					}
					else if(e.isControlDown()) {
						int rowIndex=tbl.rowAtPoint(e.getPoint());
						if(tbl.isRowSelected(rowIndex)) {
							tbl.removeRowSelectionInterval(rowIndex, rowIndex);
						}
						else {
							tbl.addRowSelectionInterval(rowIndex, rowIndex);
						}
						lastClicked=rowIndex;
					}
					else {
						int count=e.getClickCount();
						Point p=new Point(e.getX(),e.getY());
						int column = tbl.columnAtPoint(p);
						int row=tbl.rowAtPoint(p);
						lastClicked=row;
						CellEditor gce = tbl.getCellEditor();
						if(count==1&&gce==null) {
							tbl.clearSelection();
							tbl.setRowSelectionInterval(row, row);
						}
						else if(count==2) {
							tbl.clearSelection();
							tbl.setRowSelectionInterval(row, row);
							tbl.editCellAt(row, column);
							JTextField jtf = (JTextField)tbl.getEditorComponent();
							jtf.getCaret().setVisible(true);
							jtf.setHorizontalAlignment(jtf.CENTER);
							jtf.requestFocus();
						}
						else if(gce!=null) {
							int editingRow=tbl.getEditingRow();
							int editingColumn=tbl.getEditingColumn();
							CEL.oldVal=(String)tbl.getValueAt(editingRow, editingColumn);
							CEL.editingRow=editingRow;
							CEL.editingColumn=editingColumn;
							if(JOptionPane.showConfirmDialog(TabloForm.this,"Fatura Verilerini Kalýcý Olarak Düzenlemek Ýstiyormusunuz ?","Fatura Düzenle",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
								gce.stopCellEditing();
							else
								gce.cancelCellEditing();
						}
					}
				}
				
			}
			
			public void mouseReleased(MouseEvent e) {
				if(e.getButton()==e.BUTTON3) {
					tbl.requestFocus();
					TableCellEditor tce = tbl.getCellEditor();
					if(tce!=null)
						tce.cancelCellEditing();
					int row =tbl.rowAtPoint(e.getPoint());
					
					int[] selectedRows = tbl.getSelectedRows();
					if(!tbl.isRowSelected(row)||selectedRows.length<=1) {
						tbl.clearSelection();
						if(row!=-1)
						tbl.addRowSelectionInterval(row, row);
						else
							return;
						i1.setVisible(true);
						i2.setVisible(true);
					}
					else
					{
						
						i1.setVisible(false);
						i2.setVisible(false);
					}
					mpumial.selectedRow=row;
					pum.show(tbl, e.getX(), e.getY());
				}
				trd.dragStartedRow=-1;
			}
		});

		b0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ekleTur=tf1.getText(),ekleTarih=tf2.getText(),ekleMiktar=tf3.getText();
				if(ekleTur.equals("")||ekleMiktar.equals(""))
				{
					JOptionPane.showMessageDialog(TabloForm.this, "Tür veya Miktar Boþ Býrakýlamaz.","BOÞ TÜR VEYA MÝKTAR",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					ffh.faturaEkle(ekleTur, ekleTarih, ekleMiktar);
				} catch (IOException e1) {
					JOptionPane.showConfirmDialog(TabloForm.this,e1.getStackTrace() ,e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				int nextrow=0;
				while (nextrow < gelenFaturaAdet) {
					if(ekleTur.equalsIgnoreCase((String)tbl.getValueAt(nextrow, 0)))
						break;
					nextrow++;
				}
				int ydknextrow=nextrow;
				while(ydknextrow < gelenFaturaAdet && ekleTur.equalsIgnoreCase((String)tbl.getValueAt(ydknextrow, 0))) {
 					if(ekleTarih.equals((String)tbl.getValueAt(ydknextrow, 1))) {
						DecimalFormat df = new DecimalFormat();
						df.setMaximumFractionDigits(2);
						df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.UK));
						df.setGroupingUsed(false);
						
						faturaList.get(ekleTur).get(ekleTarih.substring(6,10)).get(ekleTarih.substring(3,5)).put(ekleTarih.substring(0,2),df.format(new BigDecimal((String)tbl.getValueAt(ydknextrow, 2)).add(new BigDecimal(ekleMiktar))));
						tbl.setValueAt(df.format(new BigDecimal((String)tbl.getValueAt(ydknextrow, 2)).add(new BigDecimal(ekleMiktar))), ydknextrow, 2);
						sp.getVerticalScrollBar().setValue(ydknextrow*tbl.getRowHeight());
						tbl.setRowSelectionInterval(ydknextrow, ydknextrow);
						JOptionPane.showMessageDialog(TabloForm.this, "Fatura Baþarýyla Eklendi. (Zaten Ayný Tür Ve Tarihli Fatura Bulunmaktaydý. Fatura Miktarý Arttýrýldý.)","Fatura Ekle",1);
						return;
					}
					ydknextrow++;
				}
				if(nextrow>=gelenFaturaAdet) {
					String Yil=ekleTarih.substring(6,10),Ay=ekleTarih.substring(3,5),Gun=ekleTarih.substring(0,2);
					if(!faturaList.containsKey(ekleTur))
						faturaList.put(ekleTur, new HashMap<String,HashMap<String,HashMap<String,String>>>());
					if(!faturaList.get(ekleTur).containsKey(Yil))
						faturaList.get(ekleTur).put(Yil, new HashMap<String,HashMap<String,String>>());
					if(!faturaList.get(ekleTur).get(Yil).containsKey(Ay))
						faturaList.get(ekleTur).get(Yil).put(Ay,new HashMap<String,String>());
					if(!faturaList.get(ekleTur).get(Yil).get(Ay).containsKey(Gun))
						faturaList.get(ekleTur).get(Yil).get(Ay).put(Gun,ekleMiktar);
					mdl.insertRow(0,new Object[] {ekleTur,ekleTarih,ekleMiktar});
					sp.getVerticalScrollBar().setValue(0);
					tbl.setRowSelectionInterval(0, 0);
					gelenFaturaAdet++;
				}
				else {
					String Yil=ekleTarih.substring(6,10),Ay=ekleTarih.substring(3,5),Gun=ekleTarih.substring(0,2);
					if(!faturaList.containsKey(ekleTur))
						faturaList.put(ekleTur, new HashMap<String,HashMap<String,HashMap<String,String>>>());
					if(!faturaList.get(ekleTur).containsKey(Yil))
						faturaList.get(ekleTur).put(Yil, new HashMap<String,HashMap<String,String>>());
					if(!faturaList.get(ekleTur).get(Yil).containsKey(Ay))
						faturaList.get(ekleTur).get(Yil).put(Ay,new HashMap<String,String>());
					if(!faturaList.get(ekleTur).get(Yil).get(Ay).containsKey(Gun))
						faturaList.get(ekleTur).get(Yil).get(Ay).put(Gun,ekleMiktar);
					mdl.insertRow(nextrow, new Object[] {ekleTur,ekleTarih,ekleMiktar});
					sp.getVerticalScrollBar().setValue(nextrow*tbl.getRowHeight());
					tbl.setRowSelectionInterval(nextrow,nextrow);
					gelenFaturaAdet++;
				}
				JOptionPane.showMessageDialog(TabloForm.this, "Fatura Baþarýyla Eklendi.","Fatura Ekle",1);
			}
		});
		tf4.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar()==e.VK_ENTER)
					b1.doClick();
			}
		});
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] src= tf4.getText().split(" ");
				{
					int i=0;
					for(;i<src.length;i++)
						if(!src[i].equals(""))
							break;
					if(i==src.length) {
						tumFaturaAl(mdl);
						return;
					}
				}
				mdl.getDataVector().removeAllElements();
				mdl.fireTableDataChanged();
				Set Turler =faturaList.keySet();
				Iterator<String> itTur=Turler.iterator();
				int nextFatura=0;
				while(itTur.hasNext()) {
					String Tur=itTur.next();
					Set Yillar = faturaList.get(Tur).keySet();
					Iterator<String> itYil=Yillar.iterator();
					while(itYil.hasNext()) {
						String Yil=itYil.next();
						Set Aylar = faturaList.get(Tur).get(Yil).keySet();
						Iterator<String> itAy=Aylar.iterator();
						while(itAy.hasNext()) {
							String Ay=itAy.next();
							Set Gunler = faturaList.get(Tur).get(Yil).get(Ay).keySet();
							Iterator<String> itGun=Gunler.iterator();
							while(itGun.hasNext()) {
								String Gun = itGun.next();
								String Miktar =faturaList.get(Tur).get(Yil).get(Ay).get(Gun);
								for(int i=0;i<src.length;i++)
								if(!src[i].equals("")&&(Tur.contains(src[i])||(Gun+"."+Ay+"."+Yil).contains(src[i])||Miktar.contains(src[i]))) {
									mdl.addRow(new Object[] {Tur,Gun+"."+Ay+"."+Yil,Miktar});
									break;
								}
								nextFatura++;
							}
						}
					}
				}
			}
		});
		mpumial.deleteSelectedRows=deleteSelectedRows;
		mpumial.tbl=tbl;
		i0.addActionListener(mpumial);
		i1.addActionListener(mpumial);
		i2.addActionListener(mpumial);
		add(l1);
		add(l2);
		add(l3);
		//add(l4);
		add(b0);
		add(b1);
		add(tf1);
		add(tf2);
		add(tf3);
		add(tf4);
		add(sp);
		setVisible(true);
	}
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	TabloForm tf = new TabloForm();
            }
        });
	}
}
class tableRowDrag extends MouseAdapter{
	public int dragStartedRow=-1;
	JTable tbl;
	MyCellEditorListener CEL;
	tableRowDrag(JTable tb,MyCellEditorListener c){
		tbl=tb;
		CEL=c;
	}
	public void mouseDragged(MouseEvent e) {
		tbl.requestFocus();
		if(tbl.getCellEditor()==null&&e.getModifiers()==e.BUTTON1_MASK) {
			Point p=e.getPoint();
			int rowIndex=0;
			if(tbl.inside(p.x, p.y))
				rowIndex=tbl.rowAtPoint(e.getPoint());
			else {
				e.consume();
				return;
			}
			if(dragStartedRow==-1)
				dragStartedRow=rowIndex;
			tbl.setRowSelectionInterval(dragStartedRow,rowIndex);
		}
		else if(tbl.getCellEditor()!=null) {
			int editingRow=tbl.getEditingRow();
			int editingColumn=tbl.getEditingColumn();
			CEL.oldVal=(String)tbl.getValueAt(editingRow, editingColumn);
			CEL.editingRow=editingRow;
			CEL.editingColumn=editingColumn;
			if(JOptionPane.showConfirmDialog(tbl.getTopLevelAncestor(),"Fatura Verilerini Kalýcý Olarak Düzenlemek Ýstiyormusunuz ?","Fatura Düzenle",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
				tbl.getCellEditor().stopCellEditing();
			else
				tbl.getCellEditor().cancelCellEditing();
		}
	}
}
class MyCellEditorListener implements CellEditorListener{
		int editingRow,editingColumn;
		String oldVal;
		TabloForm frm;
		JTable tbl;
		public MyCellEditorListener(TabloForm f,JTable t) {
			frm=f;
			tbl=t;
		}
		public void editingStopped(ChangeEvent e) {
			DefaultCellEditor dce = (DefaultCellEditor)e.getSource();
			JTextField jtf= (JTextField)dce.getComponent();
			if(!oldVal.equals(jtf.getText())&&!jtf.getText().equals("")) {
				String tasinacakTur=(String)tbl.getValueAt(editingRow, 0);
				String tasinacakTarih=(String)tbl.getValueAt(editingRow, 1);
				String tasinacakMiktar=(String)tbl.getValueAt(editingRow, 2);
				if(editingColumn==0) {
					try {
						frm.ffh.faturaSil(oldVal,tasinacakTarih);
						frm.ffh.faturaEkle(tasinacakTur,tasinacakTarih , tasinacakMiktar);
					} catch (IOException e1) {}
				}
				else if(editingColumn==1) {
					try {
						frm.ffh.faturaSil(tasinacakTur,oldVal);
						frm.ffh.faturaEkle(tasinacakTur, tasinacakTarih, tasinacakMiktar);
					} catch (IOException e1) {}
				}
				else {
					try {
					frm.ffh.faturaSil(tasinacakTur, tasinacakTarih);
					frm.ffh.faturaEkle(tasinacakTur, tasinacakTarih, tasinacakMiktar);
					}catch(IOException e1) {}
				}
			}
			else {
				tbl.setValueAt(oldVal, editingRow, editingColumn);
			}
		}
		
		public void editingCanceled(ChangeEvent e) {
			
		}
}

class MyPumiActionListener implements ActionListener{
	int selectedRow;
	ActionListener deleteSelectedRows;
	JTable tbl;
	public void actionPerformed(ActionEvent e) {
		JMenuItem it =(JMenuItem)e.getSource();
		TabloForm mytf = (TabloForm)tbl.getTopLevelAncestor();
		switch(it.getName()) {
			case "i0":
				deleteSelectedRows.actionPerformed(e);
				break;
			case "i1":
				TabloForm2 mtf2=new TabloForm2((String)tbl.getValueAt(selectedRow, 0), (String)tbl.getValueAt(selectedRow, 1), (String)tbl.getValueAt(selectedRow, 2));
				mytf.setEnabled(false);
				mtf2.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						mytf.setVisible(false);
						mytf.setEnabled(true);
						mytf.setVisible(true);
					}
				});
				break;
			case "i2":
				String Tur=(String)tbl.getValueAt(selectedRow, 0);
				String Yil=(String)tbl.getValueAt(selectedRow, 1);
				Yil=Yil.substring(6);
				JDialog dia=new JDialog();
				dia.setAlwaysOnTop(true);
				mytf.setEnabled(false);
				dia.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						mytf.setEnabled(true);;
					}
				});
				dia.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Toolkit t= Toolkit.getDefaultToolkit();
				dia.setLayout(null);
				dia.setResizable(false);
				dia.setSize(t.getScreenSize().width*4/5,t.getScreenSize().height*3/4);
				dia.setLocation(t.getScreenSize().width/2-dia.getWidth()/2,t.getScreenSize().height/2-dia.getHeight()*3/5);

				JLabel l1 = new JLabel(Tur.concat(" - ".concat(Yil)));
				
				DefaultTableModel tbl2model=new DefaultTableModel(0,6) {
					public boolean isCellEditable(int rowIndex, int columnIndex) {return false;}	
				};
				JTable tbl2 = new JTable(tbl2model);
				((DefaultTableCellRenderer)tbl2.getDefaultRenderer(tbl2.getColumnClass(0))).setHorizontalAlignment(SwingConstants.CENTER);

				JScrollPane jsp = new JScrollPane(tbl2);
				jsp.setBackground(null);
				l1.setSize(dia.getWidth(),40);
				l1.setFont(new Font("Arial Black",Font.TRUETYPE_FONT,24));
				l1.setHorizontalAlignment(SwingConstants.CENTER);
				
				jsp.setLocation(0,l1.getY()+l1.getHeight()+10);
				jsp.setSize(dia.getWidth(),dia.getHeight()-tbl2.getX() );
				tbl2.setBackground(new Color(255,245,225));
				tbl2.setCellSelectionEnabled(false);
				for(MouseListener ml : tbl2.getMouseListeners())
					tbl2.removeMouseListener(ml);
				for(MouseMotionListener mml : tbl2.getMouseMotionListeners())
					tbl2.removeMouseMotionListener(mml);
				tbl2.getTableHeader().setReorderingAllowed(false);
				tbl2.getTableHeader().setResizingAllowed(false);
				tbl2.setRowHeight(36);
				//tbl2.setShowGrid(false);
				tbl2.getColumnModel().getColumn(0).setHeaderValue("BAZ");
				tbl2.getColumnModel().getColumn(1).setHeaderValue("GELEN FATURA ADEDÝ");
				tbl2.getColumnModel().getColumn(2).setHeaderValue("TOPLAM FATURA TUTARI");
				tbl2.getColumnModel().getColumn(3).setHeaderValue("YAPILAN ÖDEME ADEDÝ");
				tbl2.getColumnModel().getColumn(4).setHeaderValue("TOPLAM ÖDEME TUTARI");
				tbl2.getColumnModel().getColumn(5).setHeaderValue("KALAN ÖDEME TUTARI");
				Float[] faturaTutarlar = new Float[13];
				Float[] odemeTutarlar = new Float[13];
				Integer[] faturaAdetler= new Integer[13];
				Integer[] odemeAdetler= new Integer[13];
				for(int i=0;i<13;i++) {
					faturaTutarlar[i]=new Float(0);
					odemeTutarlar[i]=new Float(0);
					faturaAdetler[i]=new Integer(0);
					odemeAdetler[i]=new Integer(0);
				}
				try {
					BufferedInputStream inext = new BufferedInputStream(new FileInputStream(new File(System.getProperty("user.dir")+"\\faturalar\\"+Tur+".txt")));
					StringBuilder nextReaden= new StringBuilder();
					while(FaturaFileHandler.satirAl(inext, nextReaden) ){
						if(nextReaden.substring(6,10).equals(Yil)) {
							String[] splitted = nextReaden.toString().split(" ");
							{
								int Ay =Integer.valueOf(splitted[0].substring(3,5)).intValue();
								float Miktar = Float.valueOf(splitted[0].substring(splitted[0].indexOf(':')+1)).floatValue();
								faturaAdetler[0] = new Integer(faturaAdetler[0].intValue()+1);
								faturaTutarlar[0] = new Float(faturaTutarlar[0].floatValue()+Miktar);
								faturaAdetler[Ay] = new Integer(faturaAdetler[Ay]+1);
								faturaTutarlar[Ay] = new Float(faturaTutarlar[Ay]+Miktar);
							}
							for(int i=1;i<splitted.length;i++) {
								int Ay =Integer.valueOf(splitted[i].substring(3,5)).intValue();
								float Miktar = Float.valueOf(splitted[i].substring(splitted[i].indexOf(':')+1)).floatValue();
								odemeAdetler[Ay] = new Integer(odemeAdetler[Ay]+1);
								odemeTutarlar[Ay] = new Float(odemeTutarlar[Ay]+Miktar);
							}
						}	
					}
					inext.close();
				}
				catch(IOException exc) {}
				
				{
					String[] bazlar= new String[] {Yil,"OCAK","ÞUBAT","MART","NÝSAN","MAYIS","HAZÝRAN","TEMMUZ","AÐUSTOS","EYLÜL","EKÝM","KASIM","ARALIK"};
					for(int i=0;i<13;i++)
						tbl2model.addRow(new String[] {bazlar[i],String.valueOf(faturaAdetler[i]),String.format("%.2f", faturaTutarlar[i]),String.valueOf(odemeAdetler[i]),String.format("%.2f", odemeTutarlar[i]),String.format("%.2f", faturaTutarlar[i]-odemeTutarlar[i])});
				}
				dia.add(l1);
				dia.add(jsp);
				dia.setVisible(true);
				break;

		}
	}
}