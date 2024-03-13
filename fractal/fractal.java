
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.util.ArrayList;
import java.util.Date;

class fractal{
	private BufferedImage bi;
	private int l = 2;
	
	public Graph g;
	fractal(int n){
		this.generateFractal(n);
		System.out.println("\nend");
		JFrame f = new JFrame();
		f.setSize(500,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.g = new Graph();
		this.g.setBounds(0,0,500,500);
		this.g.setImage(this.bi);
		MyListener ml = new MyListener();
		g.addMyListener(ml);
		f.add(this.g);
		
		
		f.setLayout(null);
		f.setVisible(true);
		
		RepaintManager rm = new RepaintManager();
		long delt = 0;
		while (true){
			Date d0 = new Date();
			f.repaint(0,0,500,500);
			Date d = new Date();
			delt = d.getTime() - d0.getTime();
			try{
				Thread.currentThread().sleep(10 - delt);
				delt = 10;
			}catch(Exception e){}
		}
	}
	private void generateFractal(int n){
		int len = (int)Math.pow(2,n);
		ArrayList<Integer> a = new ArrayList<Integer>(len);
		a.add(1); // 0 - left, 1 - up, 2 - rigth, 3 - down
		for(int i = 0; i< n; i++){
			System.out.println("generate Way" + i);
			int now = (int)Math.pow(2,i);
			ArrayList<Integer> b = new ArrayList<Integer>(a.subList(0,now));//sublist
			ArrayList<Integer> c = (ArrayList<Integer>)b.clone();
			
			/*for(int q = 0; q<now; q++){
				b.set(q, (c.get(q)+1)%4);
			}*/
			
			for(int q = 0; q<now; q++){
				b.set(q, (c.get(now-q-1)+1)%4);
			}
			a.addAll(b);
		}
		this.drawFractal(a);
		
	}
	private void drawFractal(ArrayList<Integer> a){
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		
		int x = 0;
		int y = 0;
		for(Integer i: a){
			switch(i){
				case 0:
					x--;
					break;
				case 1:
					y--;
					break;
				case 2:
					x++;
					break;
				case 3:
					y++;
					break;
				default:
			}
			if(x> maxX){
				maxX = x;
			}else if(x< minX){
				minX = x;
			}else if(y>maxY){
				maxY = y;
			}else if(y< minY){
				minY = y;
			}
		}
		System.out.println(minX+" "+maxX+" "+ minY+" "+maxY);
		this.bi = new BufferedImage((maxX-minX+1)*this.l, (maxY-minY+1)*this.l, BufferedImage.TYPE_INT_RGB);
		x = -minX*this.l;
		y = -minY*this.l;
		for(int i = 0; i< a.size(); i++){
			System.out.print("\rdrawing " + (i/(double)a.size()));
			for(int q = 0; q<this.l; q++){
				//System.out.println("coordinate"+x+" "+ y);
				this.bi.setRGB(x,y,Color.white.getRGB());
				if(a.get(i) % 2 == 0){
					x+= a.get(i)-1;
					
				}else{
					y+= a.get(i)-2;
				}
			}
		}
	}
	
	public static void main(String[] args){
		new fractal(22);
	}
	class MyListener implements SpecListener{
		private int x0 = 0;
		private int y0 = 0;
	
		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){
			if(e.getButton() == MouseEvent.BUTTON3){
				this.x0 = e.getX();
				this.y0 = e.getY();
			}
		}
		public void mouseReleased(MouseEvent e){}
		public void mouseMoved(MouseEvent e){}
		public void mouseDragged(MouseEvent e){
			if((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0){
				g.updateCenter(e.getX()-this.x0, e.getY()-this.y0);
				this.x0 = e.getX();
				this.y0 = e.getY();
			}
		}
		public void mouseWheelMoved(MouseWheelEvent e){
			double k = e.getWheelRotation()/10.0;
			//System.out.println(e.getWheelRotation()+" "+ k);
			double dx = (e.getX()-g.getCenterX())*k;
			double dy = (e.getY()-g.getCenterY())*k;
			g.updateMp(1-k);
			g.updateCenter((int)dx,(int)dy);
		}
	}
}

class Graph extends Component{
	boolean isDraw = false;
	BufferedImage bi;
	
	int centerX = 250;
	int centerY = 250;
	
	double k = 1;
	
	public void addMyListener(SpecListener sl){
		this.addMouseWheelListener(sl);
		this.addMouseListener(sl);
		this.addMouseMotionListener(sl);
	}
	public void removeMyListener(SpecListener sl){
		this.removeMouseWheelListener(sl);
		this.removeMouseListener(sl);
		this.removeMouseMotionListener(sl);
	}
	public void updateCenter(int delt_x, int delt_y){
		this.centerX += delt_x;
		this.centerY += delt_y;
	}
	public void updateMp(double k){
		this.k *= k;
	}
	public void setImage(BufferedImage b){
		this.bi = b;
		isDraw = true;
	}
	public int getCenterX(){return this.centerX;}
	public int getCenterY(){return this.centerY;}
	
	public void paint(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0,0,500,500);
		if(isDraw){
			g.drawImage(this.bi, this.centerX - (int)(0.5*this.bi.getWidth()*this.k), this.centerY - (int)(0.5*this.bi.getHeight()*k), (int)(this.bi.getWidth()*this.k), (int)(this.bi.getHeight()*this.k),null);
		}
	}
}
interface SpecListener extends MouseInputListener, MouseWheelListener{
	public void mouseClicked(MouseEvent e);
	public void mouseEntered(MouseEvent e);
	public void mouseExited(MouseEvent e);
	public void mousePressed(MouseEvent e);
	public void mouseReleased(MouseEvent e);
	public void mouseMoved(MouseEvent e);
	public void mouseDragged(MouseEvent e);
	public void mouseWheelMoved(MouseWheelEvent e);
}