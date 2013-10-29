/*
 * screener.c
 *
 *  Created on: Sep 10, 2013
 *      Author: Piyush
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>

#define BUFCHUNK 4096

//   CLIENT
//int main(int argc, char *argv[])
int client(unsigned char* fbuffer,unsigned int size,char ipaddr[],int port) {
    int sockfd;
    int len,rc,i=1;
    struct sockaddr_in address;
    int result;
    char ch = 'A';

   //Create socket for client.
    sockfd = socket(PF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("Socket creation failed\n") ;
        return -1 ;
    }

    //Name the socket as agreed with server.
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = inet_addr(ipaddr);
    address.sin_port = htons(port);
    len = sizeof(address);

    result = connect(sockfd, (struct sockaddr *)&address, len);
    if(result == -1)
    {
        perror("Error has occurred");
        return -1;
    }

    /*while ( ch < 'Y') {

        //Read and write via sockfd
        rc = write(sockfd, &ch, 1);
        printf("write rc = %d\n", rc ) ;
        if (rc == -1) break ;

        read(sockfd, &ch, 1);
        printf("Char from server = %c\n", ch);
        //if (ch == 'A') sleep(5) ;  // pause 5 seconds
    }*/
    while(ch!='X') {
    	//Read and write via sockfd
    	rc = send(sockfd,fbuffer,size,0);
    	printf("Sent = %d | ",rc);
    	if (rc == -1) {
    		printf("Error!!\n");
    		return -1;
    	}
    	recv(sockfd, &ch, 1,0);
    	fflush((FILE *)sockfd);
    	printf("Rcvd = %c\n",ch);
    }

    close(sockfd);

    return 0;
}
