malloctest: mymalloc.o mallocdriver.o
	gcc -o malloctest -m32 mymalloc.o mallocdriver.o
mymalloc.o: mymalloc.c mymalloc.h
	gcc -c -m32 mymalloc.c
mallocdriver.o: mymalloc.h mallocdriver.c
	gcc -c -m32 mallocdriver.c
touch:
	touch mymalloc.c
	touch mymalloc.h
	touch mallocdriver.c
clean:
	rm *.o
	rm malloctest
run:
	./malloctest
maltest: mallocdrv.c mymalloc.c
	gcc -o maltest -m32 mallocdrv.c mymalloc.c
malrun:
	./maltest
malclean:
	rm maltest
