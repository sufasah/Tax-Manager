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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import java.util.*;
public class TabloForm2 extends JFrame{
	HashMap<String,HashMap<String,HashMap<String,LinkedList<String>>>> faturaOdemeList;
	int gelenOdemeAdet;
	String fatura,faturaTurr,faturaTarih,faturaMiktar;
	protected FaturaFileHandler ffh=new FaturaFileHandler();
	JScrollPane sp;
	JTable tbl;
	JLabel l1,l2,l3,l4;
	JTextField tf2,tf3,tf4,tf5,tf6;
	JButton b0,b1;
	
	private void tumFaturaOdemeAl(DefaultTableModel mdl) {
		mdl.setRowCount(gelenOdemeAdet);
		int nextRow=0;

			Set Yillar = faturaOdemeList.keySet();
			Iterator<String> itYil=Yillar.iterator();
			while(itYil.hasNext()) {
				String Yil=itYil.next();
				Set Aylar = faturaOdemeList.get(Yil).keySet();
				Iterator<String> itAy=Aylar.iterator();
				while(itAy.hasNext()) {
					String Ay=itAy.next();
					Set Gunler = faturaOdemeList.get(Yil).get(Ay).keySet();
					Iterator<String> itGun=Gunler.iterator();
					while(itGun.hasNext()) {
						String Gun = itGun.next();
						LinkedList Miktarlar =faturaOdemeList.get(Yil).get(Ay).get(Gun);
						Iterator<String> itMiktar=Miktarlar.listIterator();
						while(itMiktar.hasNext()) {
							String Miktar=itMiktar.next();
							tbl.setValueAt(fatura, nextRow, 0);
							tbl.setValueAt(Gun+"."+Ay+"."+Yil, nextRow, 1);
							tbl.setValueAt(Miktar, nextRow, 2);
							nextRow++;
						}
					}
				
			}
		}
	}
	
	TabloForm2(String faturaTur,String faturaTarih,String faturaMiktar){
		faturaTur=faturaTur.toLowerCase();
		this.faturaTarih=faturaTarih;
		this.faturaTurr=faturaTur;
		this.faturaMiktar=faturaMiktar;
		String path= System.getProperty("user.dir")+"\\faturalar\\";
		fatura=faturaTur+" : "+faturaTarih+" : "+faturaMiktar;
		gelenOdemeAdet=0;
		faturaOdemeList = new HashMap<String,HashMap<String,HashMap<String,LinkedList<String>>>>() ;
		try {
				BufferedInputStream inext = new BufferedInputStream(new FileInputStream(new File(path+faturaTur+".txt")));
				StringBuilder nextReaden= new StringBuilder();
				while(ffh.satirAl(inext, nextReaden) && !faturaTarih.equals(nextReaden.substring(0,10)));
				int spaceIndex=nextReaden.indexOf(" ");
				if(spaceIndex!=-1) {
					String[] odemeler = nextReaden.substring(spaceIndex+1).split(" ");
					for(int i=0;i<odemeler.length;i++) {
						String yil=odemeler[i].substring(6,10);
						String ay=odemeler[i].substring(3,5);
						String gun=odemeler[i].substring(0,2);
						String miktar= odemeler[i].substring(odemeler[i].indexOf(':')+1);
						if(!faturaOdemeList.containsKey(yil))
							faturaOdemeList.put(yil,new HashMap<String,HashMap<String,LinkedList<String>>>());
						if(!faturaOdemeList.get(yil).containsKey(ay))
							faturaOdemeList.get(yil).put(ay, new HashMap<String,LinkedList<String>>());
						if(!faturaOdemeList.get(yil).get(ay).containsKey(gun))
							faturaOdemeList.get(yil).get(ay).put(gun, new LinkedList<String>());
						faturaOdemeList.get(yil).get(ay).get(gun).push(miktar);
						gelenOdemeAdet++;
					}
				}
				inext.close();
			
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
		pum.add(i0);
		
		l1 = new JLabel("Tarih: ",JLabel.RIGHT);
		l1.setBounds(aralikx,araliky,getWidth()/25,getHeight()/24);
		tf2 = new JTextField();
		tf2.setBounds(l1.getX()+l1.getWidth()+5, araliky, getWidth()/10, getHeight()/20);
		tf2.setText(new SimpleDateFormat("dd.MM.YYYY").format(new Date()).substring(0,10));
		
		l2 = new JLabel("Miktar: ",JLabel.RIGHT);
		l2.setBounds(tf2.getX()+tf2.getWidth()+aralikx, araliky,l1.getWidth(),l1.getHeight());
		tf3 = new JTextField();
		tf3.setBounds(l2.getX()+l2.getWidth()+5, araliky, tf2.getWidth(), tf2.getHeight());
		
		b0 = new JButton("Ekle");
		b0.setBounds(tf3.getX()+tf3.getWidth()+aralikx,araliky,getWidth()/15,tf3.getHeight());
		
		tf4 = new JTextField();
		tf4.setBounds(tf2.getX(), araliky*2+tf2.getHeight(), tf2.getWidth(), tf2.getHeight());
		b1 = new JButton("Ara");
		b1.setBounds(tf4.getX()+tf4.getWidth()+5,tf4.getY(),b0.getWidth(),tf4.getHeight());
		
		
		DefaultTableModel mdl = new DefaultTableModel(gelenOdemeAdet,3) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column==0)
					return false;
				return true;
			}
		};
		
		tbl = new JTable(mdl);
		tbl.setSurrendersFocusOnKeystroke(true);
		for(MouseListener ml : tbl.getMouseListeners())
			tbl.removeMouseListener(ml);
		for(MouseMotionListener ml : tbl.getMouseMotionListeners())
			tbl.removeMouseMotionListener(ml);
		tbl.setRowHeight(30);
		tbl.getTableHeader().setReorderingAllowed(false);
		TableColumn c0 = tbl.getColumnModel().getColumn(0);
		TableColumn c1=tbl.getColumnModel().getColumn(1);
		TableColumn c2= tbl.getColumnModel().getColumn(2);
		
		
		JTextField dce1comp = new JTextField();
		JTextField dce2comp = new JTextField();
		
		DefaultCellEditor dce1 = new DefaultCellEditor(dce1comp);
		DefaultCellEditor dce2 = new DefaultCellEditor(dce2comp);
		
		
		c1.setCellEditor(dce1);
		c2.setCellEditor(dce2);
		
		c0.setHeaderValue("Ödeme Yapýlan Fatura");
		c1.setHeaderValue("Ödeme Tarihi");
		c2.setHeaderValue("Ödeme Miktarý");
		
		DefaultTableCellRenderer centerRenderer= new DefaultTableCellRenderer() ;
		centerRenderer.setHorizontalAlignment(centerRenderer.CENTER);
		c0.setCellRenderer(centerRenderer);
		c1.setCellRenderer(centerRenderer);
		c2.setCellRenderer(centerRenderer);
		
		sp=new JScrollPane(tbl);
		sp.setLocation(0,getHeight()/5);
		sp.setSize(getWidth()*197/200,getHeight()-getHeight()*27/100);
		
		tumFaturaOdemeAl(mdl);
		
		ActionListener deleteSelectedRows = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(TabloForm2.this, "Yapýlan Ödeme Verilerini Kalýcý Olarak Silmek Ýstediðinize Eminmisiniz ?", "Fatura Ödeme Sil",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
				{
					int[] selectedRows = tbl.getSelectedRows();
					for(int i=0;i<selectedRows.length;i++) {
						String Tarih=(String)tbl.getValueAt(selectedRows[i], 1);
						String Yil = Tarih.substring(6,10);
						String Ay = Tarih.substring(3,5);
						String Gun  = Tarih.substring(0,2);
						String Miktar= (String)tbl.getValueAt(selectedRows[i], 2);
						try {
							ffh.odemeSil(faturaTurr,faturaTarih,Tarih,Miktar);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(TabloForm2.this, e1.getStackTrace(),e1.getMessage(),JOptionPane.ERROR_MESSAGE);
						}
						faturaOdemeList.get(Yil).get(Ay).get(Gun).remove(Miktar);
						if(faturaOdemeList.get(Yil).get(Ay).get(Gun).size()==0)
						faturaOdemeList.get(Yil).get(Ay).remove(Gun);
						if(faturaOdemeList.get(Yil).get(Ay).size()==0)
							faturaOdemeList.get(Yil).remove(Ay);
						if(faturaOdemeList.get(Yil).size()==0)
							faturaOdemeList.remove(Yil);
						gelenOdemeAdet--;
					}
					for(int i=0;i<selectedRows.length;i++)
						mdl.removeRow(selectedRows[i]-i);
				}
			}
		};
		
		MyCellEditorListener2 CEL = new MyCellEditorListener2(this,tbl);
		
		
		dce1.addCellEditorListener(CEL);
		dce2.addCellEditorListener(CEL);
		
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
				if(JOptionPane.showConfirmDialog(TabloForm2.this,"Fatura Verilerini Kalýcý Olarak Düzenlemek Ýstiyormusunuz ?","Fatura Düzenle",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
					tbl.getCellEditor().stopCellEditing();
				else
					tbl.getCellEditor().cancelCellEditing();
				tbl.requestFocus();
			};
		};

		dce1comp.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "stopCellEditing");
		dce2comp.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "stopCellEditing");
		dce1comp.getActionMap().put("stopCellEditing", stopCellEditing);
		dce2comp.getActionMap().put("stopCellEditing", stopCellEditing);
		
		dce1comp.addKeyListener(tarihTextTypedAction);
		dce2comp.addKeyListener(miktarTextTypedAction);
		
		tf2.addKeyListener(tarihTextTypedAction);		
		tf3.addKeyListener(miktarTextTypedAction);
		tbl.setInputMap(tbl.WHEN_FOCUSED,new InputMap());
		tbl.setInputMap(tbl.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, new InputMap());
		
		ActionMap myActionMap = new ActionMap();
		tbl.setActionMap(myActionMap);
		tbl.getInputMap(tbl.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"),"deleteSelectedRows");
		tbl.getInputMap(tbl.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl A"),"selectAllRows");
		tbl.getActionMap().put("deleteSelectedRows", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedRows.actionPerformed(e);
			}
		});
		tbl.getActionMap().put("none", bosAction);
		tbl.getActionMap().put("selectAllRows",hepsiniSec);
		
		
		tableRowDrag2 trd = new tableRowDrag2(tbl,CEL);
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
							if(jtf!=null) {
							jtf.getCaret().setVisible(true);
							jtf.setHorizontalAlignment(jtf.CENTER);
							jtf.requestFocus();
							}
						}
						else if(gce!=null) {
							int editingRow=tbl.getEditingRow();
							int editingColumn=tbl.getEditingColumn();
							CEL.oldVal=(String)tbl.getValueAt(editingRow, editingColumn);
							CEL.editingRow=editingRow;
							CEL.editingColumn=editingColumn;
							if(JOptionPane.showConfirmDialog(TabloForm2.this,"Fatura Verilerini Kalýcý Olarak Düzenlemek Ýstiyormusunuz ?","Fatura Düzenle",JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
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
					int column=tbl.columnAtPoint(e.getPoint());
					int[] selectedRows = tbl.getSelectedRows();
					
					if(!tbl.isRowSelected(row)||selectedRows.length<=1) {
						tbl.clearSelection();
						if(row!=-1)
							tbl.addRowSelectionInterval(row, row);
						else
							return;
					}
					
					pum.show(e.getComponent(), e.getX(), e.getY());
				}
				trd.dragStartedRow=-1;
			}
		});

		b0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ekleTarih=tf2.getText(),ekleMiktar=tf3.getText();
				if(ekleMiktar.equals(""))
				{
					JOptionPane.showMessageDialog(TabloForm2.this, "Tür veya Miktar Boþ Býrakýlamaz.","BOÞ TÜR VEYA MÝKTAR",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					ffh.odemeEkle(faturaTurr,faturaTarih, ekleTarih, ekleMiktar);
				} catch (IOException e1) {
					JOptionPane.showConfirmDialog(TabloForm2.this,e1.getStackTrace() ,e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				String Yil=ekleTarih.substring(6,10),Ay=ekleTarih.substring(3,5),Gun=ekleTarih.substring(0,2);
				if(!faturaOdemeList.containsKey(Yil))
					faturaOdemeList.put(Yil, new HashMap<String,HashMap<String,LinkedList<String>>>());
				if(!faturaOdemeList.get(Yil).containsKey(Ay))
					faturaOdemeList.get(Yil).put(Ay, new HashMap<String,LinkedList<String>>());
				if(!faturaOdemeList.get(Yil).get(Ay).containsKey(Gun))
					faturaOdemeList.get(Yil).get(Ay).put(Gun,new LinkedList<String>());
				faturaOdemeList.get(Yil).get(Ay).get(Gun).add(ekleMiktar);
				mdl.addRow(new Object[] {fatura,ekleTarih,ekleMiktar});
				
				sp.getVerticalScrollBar().setValue((gelenOdemeAdet+1)*tbl.getRowHeight());
				tbl.setRowSelectionInterval(gelenOdemeAdet,gelenOdemeAdet);
				gelenOdemeAdet++;
			
				JOptionPane.showMessageDialog(TabloForm2.this, "Faturaya Ödeme Baþarýyla Eklendi.","Fatura Ödeme Ekle",1);
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
						tumFaturaOdemeAl(mdl);
						return;
					}
				}
				mdl.getDataVector().removeAllElements();
				mdl.fireTableDataChanged();
				int nextFatura=0;
				Set Yillar = faturaOdemeList.keySet();
				Iterator<String> itYil=Yillar.iterator();
				while(itYil.hasNext()) {
					String Yil=itYil.next();
					Set Aylar = faturaOdemeList.get(Yil).keySet();
					Iterator<String> itAy=Aylar.iterator();
					while(itAy.hasNext()) {
						String Ay=itAy.next();
						Set Gunler = faturaOdemeList.get(Yil).get(Ay).keySet();
						Iterator<String> itGun=Gunler.iterator();
						while(itGun.hasNext()) {
							String Gun = itGun.next();
							Iterator<String> itMiktarlar =faturaOdemeList.get(Yil).get(Ay).get(Gun).listIterator();
							while(itMiktarlar.hasNext()) {
								String Miktar=itMiktarlar.next();
								for(int i=0;i<src.length;i++)
								if(!src[i].equals("")&&((Gun+"."+Ay+"."+Yil).contains(src[i])||Miktar.contains(src[i]))) {
									mdl.addRow(new Object[] {fatura,Gun+"."+Ay+"."+Yil,Miktar});
									break;
								}
								nextFatura++;
							}
						}
					}
				}
			}
			
		});
		
		tbl.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		        tbl.scrollRectToVisible(tbl.getCellRect(tbl.getRowCount()-1, 0, true));
		    }
		});
		i0.addActionListener(deleteSelectedRows);
		add(l2);
		add(l1);
		//add(l4);
		add(b0);
		add(b1);
		add(tf2);
		add(tf3);
		add(tf4);
		add(sp);
		setVisible(true);
	}
	
}
class tableRowDrag2 extends MouseAdapter{
	public int dragStartedRow=-1;
	JTable tbl;
	MyCellEditorListener2 CEL;
	tableRowDrag2(JTable tb,MyCellEditorListener2 c){
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
class MyCellEditorListener2 implements CellEditorListener{
		int editingRow,editingColumn;
		String oldVal;
		TabloForm2 frm;
		JTable tbl;
		public MyCellEditorListener2(TabloForm2 f,JTable t) {
			frm=f;
			tbl=t;
		}
		public void editingStopped(ChangeEvent e) {
			DefaultCellEditor dce = (DefaultCellEditor)e.getSource();
			JTextField jtf= (JTextField)dce.getComponent();
			if(!oldVal.equals(jtf.getText())&&!jtf.getText().equals("")) {
				String tasinacakTarih=(String)tbl.getValueAt(editingRow, 1);
				String tasinacakMiktar=(String)tbl.getValueAt(editingRow, 2);
				if(editingColumn==1) {
					try {
						frm.ffh.odemeSil(frm.faturaTurr,frm.faturaTarih,oldVal,tasinacakMiktar);
						frm.ffh.odemeEkle(frm.faturaTurr, frm.faturaTarih,tasinacakTarih, tasinacakMiktar);
					} catch (IOException e1) {}
				}
				else {
					try {
					frm.ffh.odemeSil(frm.faturaTurr,frm.faturaTarih, tasinacakTarih,oldVal);
					frm.ffh.odemeEkle(frm.faturaTurr,frm.faturaTarih, tasinacakTarih, tasinacakMiktar);
					}catch(IOException e1) {}
				}
			}
			else {
				tbl.setValueAt(oldVal, editingRow, editingColumn);
			}
		}
		
		public void editingCanceled(ChangeEvent e) {}
}

