#include <stdio.h>
#include <string.h>

#define Max 3

void check(char test[]);

struct bus{
	char start[40];
	char time[40];
	char route[1000];
};

int main(){
	struct bus root[Max];
	FILE *fp = fopen("bus2.txt","r");
	int i;
	
	for(i=0;i<Max;i++){
		//fgets(root[i].start,40, fp);
		fgets(root[i].time,40, fp);
		fgets(root[i].route,1000, fp);
		
		//check(root[i].start);
		check(root[i].time);
		check(root[i].route);
	}	
	
	fclose(fp);
	
	fp = fopen("result2.txt","w");
	
	for(i=0;i<Max;i++){
		fprintf(fp, "(\'come\', \'%s\', \'목포\', \'도림캠퍼스\', \'%s\'),", root[i].time, root[i].route);
	}
	
}

void check(char test[]){
	int i = 0;
	
	while(test[i]!='\n'){
		i++;
	}
	
	test[i] = 0;
}
