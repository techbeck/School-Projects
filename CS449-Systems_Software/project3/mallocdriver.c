#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"
int main() {
	printf("sbrk(0) %p\n", sbrk(0));
	char *a = my_firstfit_malloc(256);  // 0x100
	char *b = my_firstfit_malloc(1000); // 0x3E8
	char *c = my_firstfit_malloc(280); // 0x118
	char *d = my_firstfit_malloc(480);  // 0x1E0
	printf("a %p\n", a);
	printf("b %p\n", b);
	printf("c %p\n", c);
	printf("d %p\n", d);
	my_free(a);
	my_free(b);
	my_free(c);
	my_free(d);
	/*my_free(b);
	my_free(c);
	char *e = my_firstfit_malloc(144);	// 0x90
	printf("e %p\n", e);
	if (e < d) {
		printf("correct first fit\n");
	}
	else {
		printf("incorrect first fit\n");
	}
	my_free(a);
	my_free(e);
	my_free(d);*/
	printf("end sbrk(0) %p\n", sbrk(0));
	return 0;
}