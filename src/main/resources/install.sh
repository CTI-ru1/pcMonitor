
#copy files
sudo cp pcMonitor /etc/init.d/
sudo cp pcMonitor-1.0.one-jar.jar /root/pcMonitor-1.0.one-jar.jar
sudo cp pcmonitor.conf /root/.pcmonitor
echo "Copied files"

#add start job
sudo chmod a+x /etc/init.d/pcMonitor
sudo update-rc.d pcMonitor defaults
echo "Added start job"

#start job
sudo /etc/init.d/pcMonitor start
echo "Started job"

exit
