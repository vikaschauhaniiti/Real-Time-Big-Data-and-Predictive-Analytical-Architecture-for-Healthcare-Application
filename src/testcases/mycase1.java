package testcases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.mahout.classifier.sgd.ModelSerializer;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

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
import kafka.etl.KafkaETLKey;
import kafka.etl.KafkaETLRequest;
import kafka.etl.Props;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class mycase1 implements  ActionListener {
	static int zz=0,c1=0;
	protected static final Random RANDOM = new Random(System.currentTimeMillis());
    protected Props _props;
    @SuppressWarnings("rawtypes")
    static Producer _producer = null;
    static URI _uri = null;
    static String _topic;
    static int _count;
    static String _offsetsDir;
    static final int TCP_BUFFER_SIZE = 300000;
    static final int CONNECT_TIMEOUT = 20000;
    static final int RECONNECT_INTERVAL = Integer.MAX_VALUE;
	private static  ArrayList<String> list = new ArrayList<String>();
    static int uc=0;
    
    JPanel case01,case1;
    
	JLabel case01_name,case01_time,case01_timerl,case01_cond,case01_alarm,case01_sign,case01_value,case01_pod,case01_poi,
    hr,pulse,spo2,etco2,imco2,bps,bpd,eto2,ino2,temp,rr,ecg,case01_g;
	
    static JLabel case01_time_show, case01_time_rltime,case01_cond_show,case01_alarm_s,
    hr_s,pulse_s,spo2_s,etco2_s,imco2_s,bps_s,bpd_s,eto2_s,ino2_s,temp_s,rr_s,ecg_s,
    hr_si,pulse_si,spo2_si,etco2_si,imco2_si,bps_si,bpd_si,eto2_si,ino2_si,temp_si,rr_si,ecg_si,
    hr_sd,pulse_sd,spo2_sd,etco2_sd,imco2_sd,bps_sd,bpd_sd,eto2_sd,ino2_sd,temp_sd,rr_sd,ecg_sd;

    JButton start1,stop1,hdfs1;
    
    public JPanel createContentPane2 (String na){
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // We create a bottom JPanel to place everything on.
        JPanel totalGUI01 = new JPanel();
        totalGUI01.setLayout(null);
      // totalGUI01.setBackground(Color.cyan);
       totalGUI01.setPreferredSize(new Dimension(screenSize.width-10, 150));
        
       
       
        // Creation of a Panel to contain the title labels
        case01 = new JPanel();
        case01.setLayout(null);
        case01.setLocation(2, 0);
        case01.setSize(screenSize.width-10, 150);
        case01.setBackground(Color.cyan);
        totalGUI01.add(case01);

        case01_alarm = new JLabel("ALARM");
        case01_alarm.setLocation(0, 0);
        case01_alarm.setSize(100, 30);
        case01_alarm.setHorizontalAlignment(0);
        case01_alarm.setOpaque(true);
        case01_alarm.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_alarm);
        
        
        case01_alarm_s = new JLabel("0");
        case01_alarm_s.setLocation(100, 0);
        case01_alarm_s.setSize(200, 30);
        case01_alarm_s.setOpaque(true);
        case01_alarm_s.setBackground(Color.LIGHT_GRAY);
        case01_alarm_s.setHorizontalAlignment(0);
        case01.add(case01_alarm_s);
             
               
            
        case01_cond = new JLabel("Patient Condition : ");
        case01_cond.setLocation(0, 30);
        case01_cond.setSize(150, 30);
        case01_cond.setOpaque(true);
        case01_cond.setBackground(Color.PINK);
        case01_cond.setHorizontalTextPosition(JLabel.CENTER);
        case01_cond.setHorizontalTextPosition(JLabel.LEFT);
        case01.add(case01_cond);
        
        
        case01_cond_show = new JLabel("0");
        case01_cond_show.setLocation(150, 30);
        case01_cond_show.setSize(150, 30);
        case01_cond_show.setOpaque(true);
        case01_cond_show.setBackground(Color.pink);
        case01_cond_show.setHorizontalTextPosition(JLabel.CENTER);
        case01_cond_show.setHorizontalTextPosition(JLabel.LEFT);
        case01.add(case01_cond_show);
        
        
        start1 = new JButton("Start");
        start1.setLocation(5, 65);
        start1.setSize(100, 30);
        start1.addActionListener(this);
        case01.add(start1);
        
        stop1 = new JButton("Stop");
        stop1.setLocation(190, 65);
        stop1.setSize(100, 30);
        stop1.addActionListener(this);
        case01.add(stop1);
        
        hdfs1 = new JButton("Store Records on HDFS");
        hdfs1.setLocation(20, 100);
        hdfs1.setSize(200, 30);
        hdfs1.addActionListener(this);
        case01.add(hdfs1);
        
        case01_g = new JLabel("");
        case01_g.setLocation(300, 0);
        case01_g.setSize(165, 30);
        case01_g.setHorizontalAlignment(0);
        case01_g.setOpaque(true);
        case01_g.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_g);
        
        case01_sign = new JLabel("Signs");
        case01_sign.setLocation(300, 30);
        case01_sign.setSize(165, 30);
        case01_sign.setHorizontalAlignment(0);
        case01_sign.setOpaque(true);
        case01_sign.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_sign);
        
        case01_value = new JLabel("Value");
        case01_value.setLocation(300, 60);
        case01_value.setSize(165, 30);
        case01_value.setHorizontalAlignment(0);
        case01_value.setOpaque(true);
        case01_value.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_value);
        
        case01_poi = new JLabel("Expected(5-min)");
        case01_poi.setLocation(300, 90);
        case01_poi.setSize(165, 30);
        case01_poi.setHorizontalAlignment(0);
        case01_poi.setOpaque(true);
        case01_poi.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_poi);
        
        case01_pod = new JLabel("Accuracy(%)");
        case01_pod.setLocation(300, 120);
        case01_pod.setSize(165, 30);
        case01_pod.setHorizontalAlignment(0);
        case01_pod.setOpaque(true);
        case01_pod.setBackground(Color.LIGHT_GRAY);
        case01.add(case01_pod);
        
        
        
        case01_name = new JLabel(na);
        case01_name.setLocation(385, 0);
        case01_name.setSize(300, 30);
        case01_name.setHorizontalAlignment(0);
        case01_name.setBackground(Color.LIGHT_GRAY);
        //case01_name.setForeground(Color.white);
        case01.add(case01_name);
        
        case01_time = new JLabel("Time : ");
        case01_time.setLocation(685, 0);
        case01_time.setSize(70, 30);
        case01_time.setOpaque(true);
        case01_time.setBackground(Color.YELLOW);
        case01_time.setHorizontalAlignment(0);
        case01.add(case01_time);
        
        
        case01_time_show = new JLabel("0");
        case01_time_show.setLocation(755, 0);
        case01_time_show.setSize(230, 30);
        case01_time_show.setOpaque(true);
        case01_time_show.setBackground(Color.yellow);
        case01_time_show.setHorizontalTextPosition(JLabel.CENTER);
        case01_time_show.setHorizontalTextPosition(JLabel.LEFT);
        case01.add(case01_time_show);
        
        case01_timerl = new JLabel("Relative Time (ms) : ");
        case01_timerl.setLocation(985, 0);
        case01_timerl.setSize(150, 30);
        case01_timerl.setOpaque(true);
        case01_timerl.setBackground(Color.LIGHT_GRAY);
        case01_timerl.setHorizontalAlignment(0);
        case01.add(case01_timerl);
        
        
        case01_time_rltime = new JLabel("0");
        case01_time_rltime.setLocation(1135, 0);
        case01_time_rltime.setSize(220, 30);
        case01_time_rltime.setOpaque(true);
        case01_time_rltime.setBackground(Color.LIGHT_GRAY);
        case01_time_rltime.setHorizontalTextPosition(JLabel.CENTER);
        case01_time_rltime.setHorizontalTextPosition(JLabel.LEFT);
        case01.add(case01_time_rltime);
        
        
        hr = new JLabel("Heart Rate");
        hr.setLocation(465, 30);
        hr.setSize(80, 30);
        hr.setOpaque(true);
        hr.setBackground(Color.PINK);
        hr.setHorizontalAlignment(0);
        case01.add(hr);
        
        
        hr_s = new JLabel("0");
        hr_s.setLocation(465, 60);
        hr_s.setSize(80, 30);
        hr_s.setOpaque(true);
        hr_s.setBackground(Color.PINK);
        hr_s.setHorizontalAlignment(0);
        case01.add(hr_s);
        
        hr_si = new JLabel("0");
        hr_si.setLocation(465, 90);
        hr_si.setSize(80, 30);
        hr_si.setOpaque(true);
        hr_si.setBackground(Color.PINK);
        hr_si.setHorizontalAlignment(0);
        case01.add(hr_si);
        
        hr_sd = new JLabel("-");
        hr_sd.setLocation(465, 120);
        hr_sd.setSize(80, 30);
        hr_sd.setOpaque(true);
        hr_sd.setBackground(Color.PINK);
        hr_sd.setHorizontalAlignment(0);
        case01.add(hr_sd);
        
        
        
        
        
        spo2 = new JLabel("SPo2");
        spo2.setLocation(545, 30);
        spo2.setSize(80, 30);
        spo2.setOpaque(true);
        spo2.setBackground(Color.orange);
        spo2.setHorizontalAlignment(0);
        case01.add(spo2);
        
        
        spo2_s = new JLabel("0");
        spo2_s.setLocation(545, 60);
        spo2_s.setSize(80, 30);
        spo2_s.setOpaque(true);
        spo2_s.setBackground(Color.orange);
        spo2_s.setHorizontalAlignment(0);
        case01.add(spo2_s);
        
        spo2_si = new JLabel("0");
        spo2_si.setLocation(545, 90);
        spo2_si.setSize(80, 30);
        spo2_si.setOpaque(true);
        spo2_si.setBackground(Color.orange);
        spo2_si.setHorizontalAlignment(0);
        case01.add(spo2_si);
        
        spo2_sd = new JLabel("0");
        spo2_sd.setLocation(545, 120);
        spo2_sd.setSize(80, 30);
        spo2_sd.setOpaque(true);
        spo2_sd.setBackground(Color.orange);
        spo2_sd.setHorizontalAlignment(0);
        case01.add(spo2_sd);

        etco2 = new JLabel("etCo2");
        etco2.setLocation(625, 30);
        etco2.setSize(80, 30);
        etco2.setOpaque(true);
        etco2.setBackground(Color.PINK);
        etco2.setHorizontalAlignment(0);
        case01.add(etco2);
        
        
        etco2_s = new JLabel("0");
        etco2_s.setLocation(625, 60);
        etco2_s.setSize(80, 30);
        etco2_s.setOpaque(true);
        etco2_s.setBackground(Color.PINK);
        etco2_s.setHorizontalAlignment(0);
        case01.add(etco2_s);
        
        etco2_si = new JLabel("0");
        etco2_si.setLocation(625, 90);
        etco2_si.setSize(80, 30);
        etco2_si.setOpaque(true);
        etco2_si.setBackground(Color.PINK);
        etco2_si.setHorizontalAlignment(0);
        case01.add(etco2_si);
        
        etco2_sd = new JLabel("0");
        etco2_sd.setLocation(625, 120);
        etco2_sd.setSize(80, 30);
        etco2_sd.setOpaque(true);
        etco2_sd.setBackground(Color.PINK);
        etco2_sd.setHorizontalAlignment(0);
        case01.add(etco2_sd);
        
        
        
        imco2 = new JLabel("imCo2");
        imco2.setLocation(705, 30);
        imco2.setSize(80, 30);
        imco2.setOpaque(true);
        imco2.setBackground(Color.orange);
        imco2.setHorizontalAlignment(0);
        case01.add(imco2);
        
        
        imco2_s = new JLabel("0");
        imco2_s.setLocation(705, 60);
        imco2_s.setSize(80, 30);
        imco2_s.setOpaque(true);
        imco2_s.setBackground(Color.orange);
        imco2_s.setHorizontalAlignment(0);
        case01.add(imco2_s);
        
        imco2_si = new JLabel("0");
        imco2_si.setLocation(705, 90);
        imco2_si.setSize(80, 30);
        imco2_si.setOpaque(true);
        imco2_si.setBackground(Color.orange);
        imco2_si.setHorizontalAlignment(0);
        case01.add(imco2_si);
        
        imco2_sd = new JLabel("0");
        imco2_sd.setLocation(705, 120);
        imco2_sd.setSize(80, 30);
        imco2_sd.setOpaque(true);
        imco2_sd.setBackground(Color.orange);
        imco2_sd.setHorizontalAlignment(0);
        case01.add(imco2_sd);
        
        
        bps = new JLabel("BP (Sys)");
        bps.setLocation(785, 30);
        bps.setSize(80, 30);
        bps.setOpaque(true);
        bps.setBackground(Color.PINK);
        bps.setHorizontalAlignment(0);
        case01.add(bps);
        
        
        bps_s = new JLabel("0");
        bps_s.setLocation(785, 60);
        bps_s.setSize(80, 30);
        bps_s.setOpaque(true);
        bps_s.setBackground(Color.PINK);
        bps_s.setHorizontalAlignment(0);
        case01.add(bps_s);
        
        bps_si = new JLabel("0");
        bps_si.setLocation(785, 90);
        bps_si.setSize(80, 30);
        bps_si.setOpaque(true);
        bps_si.setBackground(Color.PINK);
        bps_si.setHorizontalAlignment(0);
        case01.add(bps_si);
        
        bps_sd = new JLabel("0");
        bps_sd.setLocation(785, 120);
        bps_sd.setSize(80, 30);
        bps_sd.setOpaque(true);
        bps_sd.setBackground(Color.PINK);
        bps_sd.setHorizontalAlignment(0);
        case01.add(bps_sd);
        
        bpd = new JLabel("BP (Dia)");
        bpd.setLocation(865, 30);
        bpd.setSize(80, 30);
        bpd.setOpaque(true);
        bpd.setBackground(Color.orange);
        bpd.setHorizontalAlignment(0);
        case01.add(bpd);
        
        
        bpd_s = new JLabel("0");
        bpd_s.setLocation(865, 60);
        bpd_s.setSize(80, 30);
        bpd_s.setOpaque(true);
        bpd_s.setBackground(Color.orange);
        bpd_s.setHorizontalAlignment(0);
        case01.add(bpd_s);
        
        bpd_si = new JLabel("0");
        bpd_si.setLocation(865, 90);
        bpd_si.setSize(80, 30);
        bpd_si.setOpaque(true);
        bpd_si.setBackground(Color.orange);
        bpd_si.setHorizontalAlignment(0);
        case01.add(bpd_si);
        
        bpd_sd = new JLabel("0");
        bpd_sd.setLocation(865, 120);
        bpd_sd.setSize(80, 30);
        bpd_sd.setOpaque(true);
        bpd_sd.setBackground(Color.orange);
        bpd_sd.setHorizontalAlignment(0);
        case01.add(bpd_sd);
        
        eto2 = new JLabel("etO2");
        eto2.setLocation(945, 30);
        eto2.setSize(80, 30);
        eto2.setOpaque(true);
        eto2.setBackground(Color.PINK);
        eto2.setHorizontalAlignment(0);
        case01.add(eto2);
        
        
        eto2_s = new JLabel("0");
        eto2_s.setLocation(945, 60);
        eto2_s.setSize(80, 30);
        eto2_s.setOpaque(true);
        eto2_s.setBackground(Color.PINK);
        eto2_s.setHorizontalAlignment(0);
        case01.add(eto2_s);
        
        
        eto2_si = new JLabel("0");
        eto2_si.setLocation(945, 90);
        eto2_si.setSize(80, 30);
        eto2_si.setOpaque(true);
        eto2_si.setBackground(Color.PINK);
        eto2_si.setHorizontalAlignment(0);
        case01.add(eto2_si);
        
        
        eto2_sd = new JLabel("0");
        eto2_sd.setLocation(945, 120);
        eto2_sd.setSize(80, 30);
        eto2_sd.setOpaque(true);
        eto2_sd.setBackground(Color.PINK);
        eto2_sd.setHorizontalAlignment(0);
        case01.add(eto2_sd);
        
        
        ino2 = new JLabel("inO2");
        ino2.setLocation(1025, 30);
        ino2.setSize(80, 30);
        ino2.setOpaque(true);
        ino2.setBackground(Color.orange);
        ino2.setHorizontalAlignment(0);
        case01.add(ino2);
        
        
        ino2_s = new JLabel("0");
        ino2_s.setLocation(1025, 60);
        ino2_s.setSize(80, 30);
        ino2_s.setOpaque(true);
        ino2_s.setBackground(Color.orange);
        ino2_s.setHorizontalAlignment(0);
        case01.add(ino2_s);
        

        ino2_si = new JLabel("0");
        ino2_si.setLocation(1025, 90);
        ino2_si.setSize(80, 30);
        ino2_si.setOpaque(true);
        ino2_si.setBackground(Color.orange);
        ino2_si.setHorizontalAlignment(0);
        case01.add(ino2_si);
        

        ino2_sd = new JLabel("0");
        ino2_sd.setLocation(1025, 120);
        ino2_sd.setSize(80, 30);
        ino2_sd.setOpaque(true);
        ino2_sd.setBackground(Color.orange);
        ino2_sd.setHorizontalAlignment(0);
        case01.add(ino2_sd);
        
        
     
        
        pulse = new JLabel("Pulse Rate");
        pulse.setLocation(1105, 30);
        pulse.setSize(80, 30);
        pulse.setOpaque(true);
        pulse.setBackground(Color.PINK);
        pulse.setHorizontalAlignment(0);
        case01.add(pulse);
        
        
        pulse_s = new JLabel("0");
        pulse_s.setLocation(1105, 60);
        pulse_s.setSize(80, 30);
        pulse_s.setOpaque(true);
        pulse_s.setBackground(Color.PINK);
        pulse_s.setHorizontalAlignment(0);
        case01.add(pulse_s);
        
        pulse_si = new JLabel("0");
        pulse_si.setLocation(1105, 90);
        pulse_si.setSize(80, 30);
        pulse_si.setOpaque(true);
        pulse_si.setBackground(Color.PINK);
        pulse_si.setHorizontalAlignment(0);
        case01.add(pulse_si);
        
        pulse_sd = new JLabel("0");
        pulse_sd.setLocation(1105, 120);
        pulse_sd.setSize(80, 30);
        pulse_sd.setOpaque(true);
        pulse_sd.setBackground(Color.PINK);
        pulse_sd.setHorizontalAlignment(0);
        case01.add(pulse_sd);
        
        
        
      
        
        rr = new JLabel("Respiration");
        rr.setLocation(1185, 30);
        rr.setSize(90, 30);
        rr.setOpaque(true);
        rr.setBackground(Color.orange);
        rr.setHorizontalAlignment(0);;
        case01.add(rr);
        
        
        rr_s = new JLabel("0");
        rr_s.setLocation(1185, 60);
        rr_s.setSize(90, 30);
        rr_s.setOpaque(true);
        rr_s.setBackground(Color.orange);
        rr_s.setHorizontalAlignment(0);
        case01.add(rr_s);
        
        rr_si = new JLabel("0");
        rr_si.setLocation(1185, 90);
        rr_si.setSize(90, 30);
        rr_si.setOpaque(true);
        rr_si.setBackground(Color.orange);
        rr_si.setHorizontalAlignment(0);
        case01.add(rr_si);
        
        rr_sd = new JLabel("0");
        rr_sd.setLocation(1185, 120);
        rr_sd.setSize(90, 30);
        rr_sd.setOpaque(true);
        rr_sd.setBackground(Color.orange);
        rr_sd.setHorizontalAlignment(0);
        case01.add(rr_sd);
        
        
        ecg = new JLabel("ECG ");
        ecg.setLocation(1265, 30);
        ecg.setSize(90, 30);
        ecg.setOpaque(true);
        ecg.setBackground(Color.PINK);
        ecg.setHorizontalAlignment(0);
        case01.add(ecg);
        
        
        ecg_s = new JLabel("0");
        ecg_s.setLocation(1265, 60);
        ecg_s.setSize(90, 30);
        ecg_s.setOpaque(true);
        ecg_s.setBackground(Color.PINK);
        ecg_s.setHorizontalAlignment(0);
        case01.add(ecg_s);
        
        ecg_si = new JLabel("0");
        ecg_si.setLocation(1265, 90);
        ecg_si.setSize(90, 30);
        ecg_si.setOpaque(true);
        ecg_si.setBackground(Color.PINK);
        ecg_si.setHorizontalAlignment(0);
        case01.add(ecg_si);
        
        ecg_sd = new JLabel("0");
        ecg_sd.setLocation(1265, 120);
        ecg_sd.setSize(90, 30);
        ecg_sd.setOpaque(true);
        ecg_sd.setBackground(Color.PINK);
        ecg_sd.setHorizontalAlignment(0);
        case01.add(ecg_sd);
    
        totalGUI01.setOpaque(true);
        return totalGUI01;
    }

    public void don(String cas)throws Exception {
		  TopologyBuilder builder = new TopologyBuilder();
	  //  String host="localhost";
	    String topic=cas;
	    String offsetpath="/home/raghav/jjj/jj";
	    String zkHostPort = "localhost:2181";
	    
	    ZkHosts zkhosts = new ZkHosts(zkHostPort);
	    
	    SpoutConfig spoutCfg = new SpoutConfig(zkhosts, topic, offsetpath, "foo");
	    
	    spoutCfg.zkServers =ImmutableList.of("127.0.0.1");
	    spoutCfg.zkPort=2181;
	   // spoutCfg.scheme=new RawMultiScheme();
	    spoutCfg.scheme = new SchemeAsMultiScheme(new StringScheme());
	    spoutCfg.zkRoot="/br😬ers";
	    spoutCfg.startOffsetTime = -1;
	    KafkaSpout kafkaSpout = new KafkaSpout(spoutCfg);
	    builder.setSpout("spout",kafkaSpout);
	 
	    builder.setBolt("fgfg", new donbolt()).shuffleGrouping("spout");
	    Config cfg = new Config();  
	    cfg.setDebug(true);
	    LocalCluster cluster= new LocalCluster();
	    cluster.submitTopology("don", cfg, builder.createTopology());
	    if(zz == 1){
			return;
		}
		
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
			if(zz == 1){
				
				
				return;
			}
			
			String sen = input.getString(0);
			
			String[] rec=sen.split(",");
			
			String []ops=rec[1].split(":");
			String []ops2=ops[2].split("_");
		
			if((Integer.parseInt(ops2[0])%60)==0 && ops2[1].equals("000")){
				list.add(rec[4]+","+rec[5]+","+rec[6]+","+rec[9]+","+rec[10]+","+rec[13]);
			}
				if(list.size()>10){
					String s=list.get(0)+","+list.get(1)+","+list.get(2)+","+list.get(3)+","+list.get(4);
					mycase1 demo5 = new mycase1();
					
					try {
						demo5.testmodel1(s);
						demo5.testmodel2(s);
						demo5.testmodel3(s);
						demo5.testmodel4(s);
						demo5.testmodel5(s);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					list.clear();
				
				
			}
			
			
			case01_alarm_s.setText("NO");
			case01_cond_show.setText(rec[0]);
			case01_time_show.setText(rec[3]);
			case01_time_rltime.setText(rec[2]);
			hr_s.setText(rec[4]);
			pulse_s.setText(rec[5]);
			spo2_s.setText(rec[6]);
			etco2_s.setText(rec[7]);
			imco2_s.setText(rec[8]);
			bps_s.setText(rec[9]);
			bpd_s.setText(rec[10]);
			eto2_s.setText(rec[11]);
			ino2_s.setText(rec[12]);
			rr_s.setText(rec[13]);
			ecg_s.setText(rec[14]);
			
			collector.ack(input);
			
			
			
		}

		@Override
		public void prepare(@SuppressWarnings("rawtypes") Map stormConf, TopologyContext context, OutputCollector outCollector) {
			// TODO Auto-generated method stub
			collector = outCollector;
			if(zz == 1){
				return;
			
			}
			
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			if(zz == 1){
				return;
			}
			
		}

	}
  
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void producer(String cas) throws Exception {
		Props props = new Props("/home/raghav/myMTPproject/test2.properties");
		_props = props;
      _topic = cas;
     
      //System.out.println("topics=" + _topic);
      
      _count = props.getInt("event.count");
      _offsetsDir = _props.getProperty("input");
      String serverUri = _props.getProperty("kafka.server.uri");
      _uri = new URI(serverUri);
     
      //System.out.println("server uri:" + _uri.toString());
      
      Properties producerProps = new Properties();
      producerProps.put("metadata.br😬er.list", String.format("%s:%d", _uri.getHost(), _uri.getPort()));
      producerProps.put("send.buffer.bytes", String.valueOf(300000));
      producerProps.put("connect.timeout.ms", String.valueOf(20000));
      producerProps.put("reconnect.interval", String.valueOf(Integer.MAX_VALUE));
      _producer = new Producer(new ProducerConfig(producerProps));
      
      File folder = new File("/home/raghav/myMTPproject/dataset/"+cas);
      File[] listOfFiles = folder.listFiles();
  	Arrays.sort(listOfFiles);

 	 BufferedReader reader = null;

 	for (int i =0; i<listOfFiles.length; i++) {
  	    File file = listOfFiles[i];
  	    String s=file.getAbsolutePath();
  	    reader = new BufferedReader(new FileReader(s));
  		String line;

  		while ((line = reader.readLine()) != null) {
  			  		 
  			byte[] bytes = (line).toString().getBytes("UTF8");
  			 _producer.send(new KeyedMessage(_topic, (Object)null, (Object)bytes));
  			 
  		//	Thread.sleep(10); 
  			 
  			if(zz == 1){
				
				
				break;
			}
  
  		}
  		if(zz == 1){
			break;
		}
  	}
  
		
  //    System.out.println(" send " + con + " " + _topic + " count events to " + _uri);
 	
      _producer.close();
      reader.close();
     // System.out.println("sending DONE");
      mycase1 demon = new mycase1();
		 demon.generateOffsets(cas);
		 
		
		 
      if(zz == 1){
			//System.out.println("sss");
			return;
		}
      


///usr/local/kafka3/bin/kafka-topics.sh --zo😬eeper localhost:2181 --delete --topic case01
      
      
  }
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void generateOffsets(String n) throws Exception {
		 Props props2 = new Props("/home/raghav/myMTPproject/test2.properties");
	
     
      //System.out.println("topics=" + _topic);
      

     String moffsetsDir = props2.getProperty("input");
      String mserverUri = props2.getProperty("kafka.server.uri");
      URI muri = new URI(mserverUri);
		
		
		
      JobConf conf = new JobConf();
      conf.set("hadoop.job.ugi", "gg");
      conf.setCompressMapOutput(false);
      Path outPath = new Path(moffsetsDir+n+"/"+n+".dat");
      FileSystem fs = outPath.getFileSystem((Configuration)conf);
      if (fs.exists(outPath)) {
          fs.delete(outPath);
      }
      System.out.println(n);
      System.out.println(muri.getHost());
      System.out.println(muri.getPort());
      
      
      KafkaETLRequest request1 = new KafkaETLRequest(n, "tcp://" + muri.getHost() + ":" + muri.getPort(), 0);
      
      //System.out.println("Dump " + request.toString() + " to " + outPath.toUri().toString());
      
      byte[] bytes = request1.toString().getBytes("UTF-8");
      
      KafkaETLKey dummyKey = new KafkaETLKey();
      
      SequenceFile.setDefaultCompressionType((Configuration)conf, (SequenceFile.CompressionType)SequenceFile.CompressionType.NONE);
      
      SequenceFile.Writer writer = SequenceFile.createWriter((FileSystem)fs, (Configuration)conf, (Path)outPath, (Class)KafkaETLKey.class, (Class)BytesWritable.class);
      
      writer.append((Writable)dummyKey, (Writable)new BytesWritable(bytes));
      
      writer.close();
      
      hdf on=new hdf();
      on.myhdfs(n);
      
  
  }
	
	public void mostart(final String ad) throws Exception{
		
		Thread t1 = new Thread(new Runnable() {
			   mycase1 demo2 = new mycase1();
			    @Override
			    public void run() {
			    	 try {System.out.println("start consumcer"+ad);
							demo2.don(ad);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    }
			});

			Thread t2 = new Thread(new Runnable() {
				mycase1 demo3 = new mycase1();
				@Override
			    public void run() {
					try {
						//System.out.println("start producer"+ad);
						demo3.producer(ad);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
			});
			t2.start();
			t1.start();
			if(zz == 1){
				
				zz=0;
				//System.out.println("stop");
				return;
			}
			
	}
	public void mostop(){
		zz=1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 mycase1 ob1 = new mycase1();
		 
		   if(e.getSource() == start1)
	        { if(c1==0){
	        	 try {
	        		 c1=1;
					ob1.mostart("case01");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }  
	        }
	       
	        else  if(e.getSource() == stop1)
	        { 
	        	 ob1.mostop();
	           c1=0;
	        }
	       
	        else  if(e.getSource() == hdfs1)
	        { 
	        	try {
					ob1.generateOffsets("case01");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	 
	           
	        }
	}
	
	
	
	public void testmodel1(String hh) throws FileNotFoundException, IOException{
	
		
	OnlineLogisticRegression classifier = ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/mod_1.model"), OnlineLogisticRegression.class);
					
		String[] p=hh.split(",");
		double[] dw1=new double[p.length];
			
			for(int op=0;op<dw1.length;op++){
				dw1[op]=Double.parseDouble(p[op]);
				
			}
			Vector veca = new RandomAccessSparseVector(dw1.length);
	        veca.assign(dw1); 
	        Vector result = classifier.classifyFull(veca);
	        
	        double r1=result.get(0);
	        double r2=result.get(1);
	    
	        
	        if(r2>r1){
	        	
	        	hr_si.setText("bradycardia");
	  		  hr_sd.setText(r2+"");
	  		}
	        else{
	        	
	        	hr_si.setText("Normal");
		  		  hr_sd.setText(r1+"");
	       }
		
	

}
	
	public void testmodel2(String hh) throws FileNotFoundException, IOException{
		
		
		OnlineLogisticRegression classifier = ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/mod_2.model"), OnlineLogisticRegression.class);
						
			String[] p=hh.split(",");
			double[] dw1=new double[p.length];
				
				for(int op=0;op<dw1.length;op++){
					dw1[op]=Double.parseDouble(p[op]);
					
				}
				Vector veca = new RandomAccessSparseVector(dw1.length);
		        veca.assign(dw1); 
		        Vector result = classifier.classifyFull(veca);
		        
		        double r1=result.get(0);
		        double r2=result.get(1);
		    
		        
		        if(r2>r1){
		        	
		        	pulse_si.setText("Tachyardia");
		        	pulse_sd.setText(r2+"");
		  		}
		        else{
		        	
		        	pulse_si.setText("Normal");
		        	pulse_sd.setText(r1+"");
		       }
	}
public void testmodel3(String hh) throws FileNotFoundException, IOException{
		
		
		OnlineLogisticRegression classifier = ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/mod_3.model"), OnlineLogisticRegression.class);
						
			String[] p=hh.split(",");
			double[] dw1=new double[p.length];
				
				for(int op=0;op<dw1.length;op++){
					dw1[op]=Double.parseDouble(p[op]);
					
				}
				Vector veca = new RandomAccessSparseVector(dw1.length);
		        veca.assign(dw1); 
		        Vector result = classifier.classifyFull(veca);
		        
		        double r1=result.get(0);
		        double r2=result.get(1);
		    
		       
		        if(r2>r1){
		        	
		        	bps_si.setText("Hypotension");
		        	bps_sd.setText(r2+"");
		  		}
		        else{
		        	
		        	bps_si.setText("Normal");
		        	bps_sd.setText(r1+"");
		       }
	}

public void testmodel4(String hh) throws FileNotFoundException, IOException{
	
	
	OnlineLogisticRegression classifier = ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/mod_4.model"), OnlineLogisticRegression.class);
					
		String[] p=hh.split(",");
		double[] dw1=new double[p.length];
			
			for(int op=0;op<dw1.length;op++){
				dw1[op]=Double.parseDouble(p[op]);
				
			}
			Vector veca = new RandomAccessSparseVector(dw1.length);
	        veca.assign(dw1); 
	        Vector result = classifier.classifyFull(veca);
	        
	        double r1=result.get(0);
	        double r2=result.get(1);
	    
	       
	        if(r2>r1){
	        	
	        	bpd_si.setText("Hypertension");
	        	bpd_sd.setText(r2+"");
	  		}
	        else{
	        	
	        	bpd_si.setText("Normal");
	        	bpd_sd.setText(r1+"");
	       }
}

public void testmodel5(String hh) throws FileNotFoundException, IOException{
	
	
	OnlineLogisticRegression classifier = ModelSerializer.readBinary(new FileInputStream("/home/raghav/myMTPproject/trainingdata/mod_5.model"), OnlineLogisticRegression.class);
					
		String[] p=hh.split(",");
		double[] dw1=new double[p.length];
			
			for(int op=0;op<dw1.length;op++){
				dw1[op]=Double.parseDouble(p[op]);
				
			}
			Vector veca = new RandomAccessSparseVector(dw1.length);
	        veca.assign(dw1); 
	        Vector result = classifier.classifyFull(veca);
	        
	        double r1=result.get(0);
	        double r2=result.get(1);
	    
	       
	        if(r2>r1){
	        	
	        	spo2_si.setText("Hypoxaemia");
	        	spo2_sd.setText(r2+"");
	  		}
	        else{
	        	
	        	spo2_si.setText("Normal");
	        	spo2_sd.setText(r1+"");
	       }
}
	
	
}


/*

😬😬😬😬😬😬😬😬😬
😬😬😬😬😬😬😬😬😬
😬😬😬                😬😬😬
😬😬😬                😬😬😬
😬😬😬                😬😬😬
😬😬😬😬😬😬😬😬😬
😬😬😬😬😬😬😬😬😬
😬😬😬
😬😬😬
😬😬😬
😬😬😬



😬😬😬😬😬😬😬😬😬
😬😬😬😬😬😬😬😬😬
                 😬😬😬                
                 😬😬😬                
                 😬😬😬   
                 😬😬😬  
                 😬😬😬 
                 😬😬😬
                 😬😬😬          
😬😬😬😬😬😬😬😬😬
😬😬😬😬😬😬😬😬😬


😬😬😬                     😬😬😬
😬😬😬                   😬😬😬
😬😬😬                😬😬😬
😬😬😬            😬😬😬
😬😬😬       😬😬😬
😬😬😬😬😬😬
😬😬😬😬😬😬
😬😬😬      😬😬😬
😬😬😬          😬😬😬
😬😬😬             😬😬😬
😬😬😬                 😬😬😬


😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬               😬😬😬
😬😬😬😬😬😬😬😬😬
😬😬😬😬😬😬😬😬😬


😬

*/