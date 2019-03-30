import socket
import RPi.GPIO as GPIO

BREAKPIN=18
STEERINGFIRSTPIN=19 #this will control left motor 
STEERINGSECONDPIN=20 #this will control right motor

GPIO.setmode(GPIO.BCM)
GPIO.setup(BREAKPIN, GPIO.OUT)
GPIO.setup(STEERINGFIRSTPIN,GPIO.OUT)
GPIO.setup(STEERINGSECONDPIN,GPIO.OUT)
GPIO.output(STEERINGFIRSTPIN,GPIO.HIGH)
GPIO.output(STEERINGSECONDPIN,GPIO.HIGH)

serversocket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
port=5678

try:
	serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	serversocket.bind(('',port))
	serversocket.listen(5)
	print("server Initiated...\n\ncall wait_and_get_msg() method to listen client requests...")
except:
	print("Host Already Their")
	
while(True):
	clientsocket,addr = serversocket.accept()
	msg = clientsocket.recv(1024).decode()
	msg=str(msg)
	input=msg[0]
	value=msg[1:]
	print(msg)
	if input=='i':
		if value==1:
                        #this will start the rotation in the left direction
			GPIO.output(STEERINGFIRSTPIN,GPIO.LOW)
			GPIO.output(STEERINGSECONDPIN,GPIO.HIGH)
		else:
                        #this will stop the rotation
			GPIO.output(STEERINGFIRSTPIN,GPIO.HIGH)
			GPIO.output(STEERINGSECONDPIN,GPIO.HIGH)
	if input=='j':
		if value==1:
                        #this will start the roatation in right direction
			GPIO.output(STEERINGFIRSTPIN,GPIO.HIGH)
			GPIO.output(STEERINGSECONDPIN,GPIO.LOW)
		else:
                        #this will stop the rotation
			GPIO.output(STEERINGFIRSTPIN,GPIO.HIGH)
			GPIO.output(STEERINGSECONDPIN,GPIO.HIGH)
	if input=='b':
		if value==1:
			GPIO.output(BREAKPIN,GPIO.HIGH)
		else:
			GPIO.output(BREAKPIN,GPIO.LOW)
	if input=='s':
		#Here the code for contorlling the speed will come 
		#you just need to set the speed of car to the value of variable value
	clientsocket.close()
	




