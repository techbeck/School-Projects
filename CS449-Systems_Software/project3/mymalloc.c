#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"
struct node {
	int status;
	struct node *previous;
	struct node *next;
	int size;
};
static void coalesce(struct node *);
static struct node *head = NULL;
static const int ALLOCATED = 1;
static const int FREE = 0;
static const int node_size = (int) sizeof(struct node);
void *my_firstfit_malloc(int size) {
	//printf("size %x\n", size);
	//printf("sbrk(0) %p\n", sbrk(0));
	struct node *this_node = head;
	struct node *new_node = NULL;
	// first allocation
	if (head == NULL) {
		head = sbrk(size + node_size);
		printf("sizeof(struct node) %x\n", node_size);
		printf("head %p\n", head);
		printf("head+ %p\n", (head + node_size));
		printf("brk- %p\n\n", (sbrk(0) - size));
		head->status = ALLOCATED;
		head->previous = NULL;
		head->next = NULL;
		head->size = size;
		return (head + node_size);
	}
	while (this_node->next != NULL) {
		//printf("loop ");
		//printf("size = %x\n", this_node->size);
		// skips allocated space
		if (this_node->status == ALLOCATED) {
			this_node = this_node->next;
			continue;
		}
		// allocating w/i freed space
		if (this_node->size >= size) {
			//printf("allocating w/i freed space\n");
			// only create new free node if there's space for it
			if (this_node->size > (size + node_size)) {
				new_node = this_node + node_size + size;
				new_node->status = FREE;
				new_node->previous = this_node;
				new_node->next = this_node->next;
				new_node->size = this_node->size - size - node_size;
				this_node->next = new_node;
				this_node->size = size;
			}
			this_node->status = ALLOCATED;
			printf("next- %p\n", (this_node->next - size));
			printf("this+ %p\n\n", (this_node + node_size));
			return (this_node + node_size);
		}
		this_node = this_node->next;
	}
	// new allocation
	this_node->next = sbrk(size + node_size);
	new_node = this_node->next;
	new_node->status = ALLOCATED;
	new_node->previous = this_node;
	new_node->next = NULL;
	new_node->size = size;
	//printf("new brk %p\n", sbrk(0));
	printf("brk- %p\n", (sbrk(0) - size));
	printf("new+ %p\n\n", (new_node + node_size));
	return (new_node + node_size);
}
void my_free(void *ptr) {
	struct node *this_node = ptr - node_size;
	this_node->status = FREE;
	printf("freeing ptr %p\n", ptr);
	coalesce(this_node);
	printf("new ptr %p\n", ptr);
	if ((ptr + this_node->size) == sbrk(0)) { // decrement brk if freed space borders it
		sbrk(-(this_node->size + node_size));
		printf("decrement\n");
	}
	else {
		printf("didn't decrement\n");
		/*//printf("sbrk %p\n", sbrk(0));
		//printf("ptr+size %p\n", (ptr + this_node->size));
		//printf("size node %x\n", node_size);*/
	}
}
static void coalesce(struct node *this_node) {
	struct node *next_node = this_node->next;
	struct node *previous_node = this_node->previous;
	if (next_node != NULL && next_node->status == FREE) {
		printf("coalesce\n");
		this_node->next = next_node->next;
		this_node->size = this_node->size + next_node->size + node_size;
	}
	if (previous_node != NULL && previous_node->status == FREE) {
		printf("coalesce\n");
		previous_node->next = this_node->next;
		previous_node->size = previous_node->size + this_node->size + node_size;
		this_node = previous_node;
	}
}