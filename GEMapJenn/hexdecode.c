#include<stdio.h>
#include<limits.h>

int main(int argc, char** argv){
	long unsigned int val;
	sscanf(argv[1], "%lx", &val);
	printf("Decimal: %lu\nHexadecimal: %lx\nBinary: ", val, val);
	int bits = sizeof(long unsigned int) * CHAR_BIT;
	for(int i = 0; i < bits; i++){
		printf("%d", (val >> bits - i - 1) & 1);
	}
	printf("\n");
	return 0;
}

