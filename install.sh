#!/bin/bash
#�������
hello(){
  time=$(date "+%Y-%m-%d %H:%M:%S")
  echo $time
}
#ֹͣtomcat
stop_tomcat(){
  /root/apache-tomcat-8.5.72/bin/./shutdown.sh
}
#�����Ŀ
package_code(){
  #�Ƚ���codeĿ¼��
  cd /root/notepad/code
  # maven clean
  mvn clean
  #��������
  sleep 2
  #maven package
  mvn package

}

#ɾ��tomcat��ԭ����war���������ƽ�ȥ�µ�war��
install_war(){
  #ɾ��ԭ��tomcat��war����ͬ����ѹĿ¼
  rm -rf /root/apache-tomcat-8.5.72/webapps/notepad.war
  rm -rf /root/apache-tomcat-8.5.72/webapps/notepad
  #�����µ�war��
  cp /root/notepad/code/target/notepad.war /root/apache-tomcat-8.5.72/webapps/
}
#����tomcat
start_tomcat(){
  /root/apache-tomcat-8.5.72/bin/./startup.sh
}
#������
main(){
  hello && stop_tomcat && package_code && install_war && start_tomcat && echo "success"
}
main