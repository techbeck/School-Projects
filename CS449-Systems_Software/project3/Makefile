malloctest: mymalloc.o mallocdriver.o
	gcc -o malloctest mymalloc.o mallocdriver.o
mymalloc.o: mymalloc.c mymalloc.h
	gcc -c mymalloc.c
mallocdriver.o: mymalloc.h mallocdriver.c
	gcc -c mallocdriver.c
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
	gcc -o maltest mallocdrv.c mymalloc.c
malclean:
	rm maltest
