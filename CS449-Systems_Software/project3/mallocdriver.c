#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"
int main() {
	void *sbrk0 = sbrk(0);
	printf("sbrk(0) %p\n", sbrk0);
	char *a = my_firstfit_malloc(100);
	char *b1 = my_firstfit_malloc(100);
	char *b2 = my_firstfit_malloc(100);
	char *c = my_firstfit_malloc(300);
	char *d = my_firstfit_malloc(400);
	char *e = my_firstfit_malloc(500);
	printf("a: %p\n", a);
	printf("b1: %p\n", b1);
	printf("b2: %p\n", b2);
	printf("c: %p\n", c);
	printf("d: %p\n", d);
	printf("e: %p\n", e);
	my_free(b1);
	my_free(b2);
	my_free(d);
	printf("freeing b1, b2, d\n");
	char *f = my_firstfit_malloc(300);
	char *g = my_firstfit_malloc(150);
	if (f < e) {
		printf("correct allocation into freed space\n");
	}
	if (g < f) {
		printf("correct coalesce & first fit\n");
	}
	printf("a: %p\n", a);
	printf("c: %p\n", c);
	printf("d: %p\n", d);
	printf("e: %p\n", e);
	printf("f: %p\n", f);
	printf("g: %p\n", g);
	my_free(a);
	my_free(c);
	my_free(d);
	my_free(e);
	my_free(f);
	my_free(g);
	void *sbrk1 = sbrk(0);
	printf("end sbrk(0) %p\n", sbrk1);
	if (sbrk0 == sbrk1) {
		printf("correct decrement\n");
	}
	else {
		printf("not decremented correctly\n");
	}
	a = my_firstfit_malloc(100);
	my_free(a);
	return 0;
}