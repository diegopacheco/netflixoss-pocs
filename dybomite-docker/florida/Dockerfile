FROM java:8-jdk
MAINTAINER Diego Pacheco - diego.pacheco.it@gmail.com

RUN apt-get update && apt-get install -y \
	autoconf \
	build-essential \
	dh-autoreconf \
	git \
	libssl-dev \
	libtool \
	python-software-properties \
	tcl8.5 \
	dos2unix \ 
	unzip \
	curl \ 
	vim

RUN curl -sL https://deb.nodesource.com/setup_0.12 | bash - && \
    apt-get install -y nodejs && \ 
    curl -L https://npmjs.org/install.sh | sh && \ 
    npm install -g grunt && \ 
    npm install -g http && \ 
    npm install -g url && \ 
    npm install -g fs 

RUN mkdir /etc/dynomite
ADD seeds.list /etc/dynomite/seeds.list

ADD start-florida.sh /usr/local/dynomite/
RUN chmod 777 /usr/local/dynomite/start-florida.sh

RUN git clone https://github.com/Netflix/dynomite.git

RUN cd /dynomite/ && git checkout tags/v0.5.6 

WORKDIR /dynomite/

EXPOSE 8080

CMD ["/user/local/dynomite/start-florida.sh"]

#
# To gen the config: 
#    python generate_yamls.py 172.18.0.101:rack1 172.18.0.102:rack2 172.18.0.103:rack3
#
# To clean up building env
#
# docker stop $(docker ps -a -q)
# docker rm `docker ps -aq`
# docker rmi -f $(docker images | grep "<none>" | awk "{print \$3}")
#
#
# to Build: $ docker build -t diegopacheco/florida .
# Network : $ docker network create --subnet=172.18.0.0/16 myDockerNetDynomite
# to Run  : $ docker run -d --net myDockerNetDynomite --name florida -e DYNOMITE_RACK=rack1 -e DYNOMITE_TOKENS=0 diegopacheco/florida
#
#