import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FaturaFileHandler {
	public boolean odemeSil(String tur,String tarih,String odemeTarihi,String odemeMiktar) throws FileNotFoundException,IOException{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+tur+".txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		int satir=0;
		while(satirAl(i,readen)) {
			int indexOdeme=readen.substring(11).indexOf(odemeTarihi+":"+odemeMiktar)+11;
			if(tarih.equals(readen.substring(0,10))){
				if(indexOdeme!=10) {
				i.close();
				i=new BufferedInputStream(new FileInputStream(dos));
				File ndos= new File(path+tur+"yenidosya.txt");
				BufferedOutputStream o= new BufferedOutputStream(new FileOutputStream(ndos));
				while(satir!=0) {
					satirAl(i,readen);
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					satir--;
				}
				satirAl(i,readen);
				int j=indexOdeme;
				while(j<readen.length()&&readen.charAt(j)!=' ')
					j++;
				o.write((readen.substring(0,indexOdeme-1).getBytes()));
				
				o.write((readen.substring(j)).getBytes());
				o.write(new byte[] {'\r','\n'});
				while(satirAl(i,readen)) {
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
				}
				i.close();
				o.close();
				dos.delete();
				ndos.renameTo(dos);
				return true;
				}
				else
					return false;
			}
			satir++;
		}
		return false;
	}
	public boolean odemeEkle(String tur,String tarih,String odemeTarihi,String odemeMiktar) throws FileNotFoundException,IOException{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+tur+".txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		int satir=0;
		while(satirAl(i,readen)) {
			if(tarih.equals(readen.substring(0,10))){
				i.close();
				i=new BufferedInputStream(new FileInputStream(dos));
				File ndos= new File(path+tur+"yenidosya.txt");
				BufferedOutputStream o= new BufferedOutputStream(new FileOutputStream(ndos));
				
				while(satir!=0) {
					satirAl(i,readen);
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					satir--;
				}
				satirAl(i,readen);
				o.write((readen+" "+odemeTarihi+":"+odemeMiktar+"\r\n").getBytes());
				while(satirAl(i,readen)) {
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
				}
				i.close();
				o.close();
				dos.delete();
				ndos.renameTo(dos);
				return true;
			}
			satir++;
		}
		return false;
	}
	public boolean odemeDuzenle(String tur,String tarih,String odemeTarihi,String odemeMiktar,String yeniMiktar) throws FileNotFoundException,IOException{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+tur+".txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		int satir=0;
		while(satirAl(i,readen)) {
			int indexOdeme=readen.substring(11).indexOf(odemeTarihi+":"+odemeMiktar)+11;
			if(tarih.equals(readen.substring(0,10))){
				if(indexOdeme!=10) {
				i.close();
				i=new BufferedInputStream(new FileInputStream(dos));
				File ndos= new File(path+tur+"yenidosya.txt");
				BufferedOutputStream o= new BufferedOutputStream(new FileOutputStream(ndos));
				while(satir!=0) {
					satirAl(i,readen);
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					satir--;
				}
				satirAl(i,readen);
				int j=indexOdeme;
				while(j<readen.length()&&readen.charAt(j)!=' ')
					j++;
				o.write((readen.substring(0,indexOdeme)+odemeTarihi+":"+yeniMiktar+readen.substring(j)).getBytes());
				o.write(new byte[] {'\r','\n'});
				while(satirAl(i,readen)) {
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
				}
				i.close();
				o.close();
				dos.delete();
				ndos.renameTo(dos);
				return true;
				}
				else
					return false;
			}
			satir++;
		}
		return false;
	}

	public boolean faturaSil(String tur,String tarih) throws IOException{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+tur+".txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		int satir=0;
		boolean fullEmptyNewFile=true;
		while(satirAl(i,readen)) {
			if(tarih.equals(readen.substring(0,10))){
				i.close();
				i=new BufferedInputStream(new FileInputStream(dos));
				File ndos= new File(path+tur+"yenidosya.txt");
				BufferedOutputStream o= new BufferedOutputStream(new FileOutputStream(ndos));
				if(satir!=0)
					fullEmptyNewFile=false;
				while(satir!=0) {
					satirAl(i,readen);
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					satir--;
				}
				satirAl(i,readen);
				if(satirAl(i,readen)) {
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					fullEmptyNewFile=false;					
				}
				while(satirAl(i,readen)) {
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
				}
				i.close();
				o.close();
				if(fullEmptyNewFile) {
					ndos.delete();
					faturaTurSil(tur);
				}
				else {
					dos.delete();
					ndos.renameTo(dos);
				}
				return true;
			}
			satir++;
		}
		return false;
	}
	public void faturaEkle(String tur,String tarih,String Miktar) throws IOException
	{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+tur+".txt");
		BufferedInputStream i;
		try {
			i=new BufferedInputStream(new FileInputStream(dos));
		}
		catch(FileNotFoundException e) {
			BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(dos));
			o.write((tarih+":"+Miktar+"\r\n").getBytes());
			o.close();
			o = new BufferedOutputStream(new FileOutputStream(new File(path+"turler.txt"),true));
			o.write((tur+"\r\n").getBytes());
			o.close();
			return;
		}
		StringBuilder readen=new StringBuilder();
		for(int count=0;satirAl(i,readen);count++) {
			if(tarih.equals(readen.substring(0, 10))) {
				int j=11;
				while(j<readen.length()&&readen.charAt(j)!=' ')
					j++;
				String gelenMiktar=readen.substring(11,j);
				String kalanstring =readen.substring(j);
				File nDos= new File(path+tur+"yenidosya.txt");
				i.close();
				i= new BufferedInputStream(new FileInputStream(dos));
				BufferedOutputStream yo = new BufferedOutputStream(new FileOutputStream(nDos));
				while(count!=0) {
					satirAl(i,readen);
					yo.write(readen.toString().getBytes());
					yo.write(new byte[]{'\r','\n'});
					count--;
				}
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.UK));
				df.setGroupingUsed(false);
				yo.write((tarih+":"+df.format(new BigDecimal(gelenMiktar).add(new BigDecimal(Miktar)))+kalanstring+"\r\n").getBytes());
				satirAl(i,readen);
				while(satirAl(i,readen)) {
					yo.write(readen.toString().getBytes());
					yo.write(new byte[]{'\r','\n'});
				}
				yo.close();
				i.close();
				dos.delete();
				nDos.renameTo(dos);
				return;
			}
		}
		i.close();
		BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(dos,true));
		o.write((tarih+":"+Miktar+"\r\n").getBytes());
		o.close();
		return;
	}
	public static boolean kelimeAl(BufferedInputStream i,StringBuilder kelime) throws IOException {
		kelime.delete(0,kelime.length());
		int cr=i.read();
		while(cr!=-1) {
			if(cr==13) {
				cr=i.read();
				if(cr==10) {
					return true;
				}
				else if(cr==-1) {
					kelime.append((char)13);
					return false;
				}
			}
			else if(cr==' ') {
				return true;
			}
			else {
				kelime.append((char)cr);
			}
			cr=i.read();
		}
		return false;
	}
	public static boolean satirAl(BufferedInputStream i,StringBuilder satir) throws IOException {
		
		satir.delete(0, satir.length());
		int cr=i.read();
		while(cr!=-1) {
			if(cr==13) {
				cr=i.read();
				if(cr==10) {
					return true;
				}
				else if(cr==-1) {
					satir.append((char)13);
					return false;
				}
			}
			else {
				satir.append((char)cr);
			}
			cr=i.read();
		}
		return false;
	}
	public boolean faturaTurSil(String tur) throws IOException{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+"turler.txt");
		File ndos=new File(path+"turleryenidosya.txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(ndos));
		StringBuilder readen=new StringBuilder();
		while(satirAl(i,readen)) {
			if(!readen.toString().equals(tur)) {
				o.write(readen.toString().getBytes());
				o.write(new byte[] {'\r','\n'});
			}
		}
		i.close();
		o.close();
		new File(path+tur+".txt").delete();
		dos.delete();
		ndos.renameTo(dos);
		return true;
	}
	public boolean faturaAdlandir(String eski,String yeni) throws IOException {
		eski = eski.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dataDos=new File(path+eski+".txt");
		if(!dataDos.exists())
			return false;
		File dos= new File(path+"turler.txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		int satir=0;
		while(satirAl(i,readen)) {
			if(readen.toString().equals(eski)) {
				i.close();
				i=new BufferedInputStream(new FileInputStream(dos));
				File ndos= new File(path+"turleryenidosya.txt");
				BufferedOutputStream o= new BufferedOutputStream(new FileOutputStream(ndos));
				while(satir!=0) {
					satirAl(i,readen);
					if(readen.toString().equals(yeni)) {
						o.close();
						ndos.delete();
						return false;
					}
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
					satir--;
				}
				satirAl(i,readen);
				o.write(yeni.getBytes());
				o.write(new byte[] {'\r','\n'});
				while(satirAl(i,readen)) {
					if(readen.toString().equals(yeni)) {
					    o.close();
						ndos.delete();
						return false;
					}
					o.write(readen.toString().getBytes());
					o.write(new byte[] {'\r','\n'});
				}
				File datanDos = new File(path+yeni+".txt");
				if(datanDos.exists())
					datanDos.delete();
				dataDos.renameTo(datanDos);
				i.close();
				o.close();
				dos.delete();
				ndos.renameTo(dos);
				return true;
			}
			satir++;
		}
		return false;
	}
	public boolean faturaTurEkle(String tur) throws IOException
	{
		tur = tur.toLowerCase();
		String path=System.getProperty("user.dir")+"\\faturalar\\";
		File dos = new File(path+"turler.txt");
		BufferedInputStream i= new BufferedInputStream(new FileInputStream(dos));
		StringBuilder readen=new StringBuilder();
		while(satirAl(i,readen)) {
			if(readen.toString().equals(tur)){
					return false;
			}
		}
		i.close();
		BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(dos,true));
		o.write(tur.getBytes());
		o.write(new byte[] {'\r','\n'});
		o.close();
		new File(path+tur+".txt").createNewFile();
		return true;
	}
}
