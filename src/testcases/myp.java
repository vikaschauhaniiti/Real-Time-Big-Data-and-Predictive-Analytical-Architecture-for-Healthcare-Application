package testcases;

import javax.swing.*;

import com.google.common.collect.ImmutableList;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Map;
import java.awt.event.ActionEvent;

public class myp implements  ActionListener{

    // Definition of global values and items that are part of the GUI.
    int redScoreAmount = 0;
    int blueScoreAmount = 0;
    static String y;
    JPanel titlePanel, scorePanel, buttonPanel;
    JLabel redLabel, blueLabel;
	static JLabel redScore;
	JLabel blueScore;
    JButton redButton, blueButton, resetButton;

    public JPanel createContentPane (){

        // We create a bottom JPanel to place everything on.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        // Creation of a Panel to contain the title labels
        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(10, 0);
        titlePanel.setSize(800, 30);
        totalGUI.add(titlePanel);

        redLabel = new JLabel("Red Team");
        redLabel.setLocation(0, 0);
        redLabel.setSize(350, 30);
        redLabel.setHorizontalAlignment(0);
        redLabel.setForeground(Color.red);
        titlePanel.add(redLabel);

        blueLabel = new JLabel("Blue Team");
        blueLabel.setLocation(400, 0);
        blueLabel.setSize(350, 30);
        blueLabel.setHorizontalAlignment(0);
        blueLabel.setForeground(Color.blue);
        titlePanel.add(blueLabel);

        // Creation of a Panel to contain the score labels.
        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(10, 40);
        scorePanel.setSize(800, 30);
        totalGUI.add(scorePanel);

        redScore = new JLabel(""+redScoreAmount);
        redScore.setLocation(0, 0);
        redScore.setSize(350, 30);
        redScore.setHorizontalAlignment(0);
        scorePanel.add(redScore);

        blueScore = new JLabel(""+blueScoreAmount);
        blueScore.setLocation(400, 0);
        blueScore.setSize(350, 30);
        blueScore.setHorizontalAlignment(0);
        scorePanel.add(blueScore);

        // Creation of a Panel to contain all the JButtons.
        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 80);
        buttonPanel.setSize(800, 70);
        totalGUI.add(buttonPanel);

        // We create a button and manipulate it using the syntax we have
        // used before. Now each button has an ActionListener which posts 
        // its action out when the button is pressed.
        redButton = new JButton("Red Score!");
        redButton.setLocation(90, 0);
        redButton.setSize(120, 30);
        redButton.addActionListener(this);
        buttonPanel.add(redButton);

        blueButton = new JButton("Blue Score!");
        blueButton.setLocation(510, 0);
        blueButton.setSize(120, 30);
        blueButton.addActionListener(this);
        buttonPanel.add(blueButton);

        resetButton = new JButton("Reset Score");
        resetButton.setLocation(300, 40);
        resetButton.setSize(250, 30);
        resetButton.addActionListener(this);
        buttonPanel.add(resetButton);
        
        totalGUI.setOpaque(true);
        return totalGUI;
    }

    // This is the new ActionPerformed Method.
    // It catches any events with an ActionListener attached.
    // Using an if statement, we can determine which button was pressed
    // and change the appropriate values in our GUI.
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == redButton)
        {
            redScoreAmount = redScoreAmount + 1;
            redScore.setText(y);
        }
        else if(e.getSource() == blueButton)
        {
            blueScoreAmount = blueScoreAmount + 1;
            blueScore.setText(""+blueScoreAmount);
        }
        else if(e.getSource() == resetButton)
        {
            redScoreAmount = 0;
            blueScoreAmount = 0;
            redScore.setText(""+redScoreAmount);
            blueScore.setText(""+blueScoreAmount);
        }
    }

    private static void createAndShowGUI() throws Exception {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("VITAL  MONITOR");

        //Create and set up the content pane.
        myp demo = new myp();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setVisible(true);
       //demo.don();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    public void don()throws Exception {
		  TopologyBuilder builder = new TopologyBuilder();
	  //  String host="localhost";
	    String topic="case";
	    String offsetpath="/home/raghav/jjj/jj";
	    String zkHostPort = "localhost:2181";
	    
	    ZkHosts zkhosts = new ZkHosts(zkHostPort);
	    
	    SpoutConfig spoutCfg = new SpoutConfig(zkhosts, topic, offsetpath, "foo");
	    
	    spoutCfg.zkServers =ImmutableList.of("127.0.0.1");
	    spoutCfg.zkPort=2181;
	   // spoutCfg.scheme=new RawMultiScheme();
	    spoutCfg.scheme = new SchemeAsMultiScheme(new StringScheme());
	    spoutCfg.zkRoot="/brokers";
	    spoutCfg.startOffsetTime = -1;
	    KafkaSpout kafkaSpout = new KafkaSpout(spoutCfg);
	    builder.setSpout("spout",kafkaSpout);
	 
	    builder.setBolt("fgfg", new donbolt()).shuffleGrouping("spout");
	    Config cfg = new Config();  
	    cfg.setDebug(true);
	    LocalCluster cluster= new LocalCluster();
	    cluster.submitTopology("don", cfg, builder.createTopology());
	  
	  }
    public static class donbolt extends BaseRichBolt{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private OutputCollector collector;
		

		@Override
		public void execute(Tuple input) {
			// TODO Auto-generated method stub
			String sen = input.getString(0);
			String[] rec=sen.split(",",-1);
			if(!rec[64].equals("0") || !rec[63].equals("0")){
				y=rec[0]+" ("+rec[2]+") ---> emergency...!! "+rec[65];
				redScore.setText(y);
				
				
			System.out.println(rec[0]+" ("+rec[3]+") ---> emergency...!! "+rec[65]);
			System.out.println("**********************\n");
			}
			else{
				y=rec[2]+"--normal";
				redScore.setText(y);
			}
			collector.ack(input);
		}

		@Override
		public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector outCollector) {
			// TODO Auto-generated method stub
			this.collector = outCollector;
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
		}

	}
}