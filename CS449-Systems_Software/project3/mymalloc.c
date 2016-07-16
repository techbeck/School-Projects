#include <stdio.h>
#include <unistd.h>
#include "mymalloc.h"
struct node {
	int status;
	struct node *previous;
	struct node *next;
	int size;
};
static struct node *coalesce(struct node *, struct node *);
static struct node *head = NULL;
static const int ALLOCATED = 1;
static const int FREE = 0;
static const int node_size = (int) sizeof(struct node);
void *my_firstfit_malloc(int size) {
	struct node *this_node = head;
	struct node *new_node = NULL;
	// first allocation
	if (head == NULL) {
		head = sbrk(size + node_size);
		head->status = ALLOCATED;
		head->previous = NULL;
		head->next = NULL;
		head->size = size;
		return (head + 1);
	}
	while (this_node->next != NULL) {
		// skips allocated space
		if (this_node->status == ALLOCATED) {
			this_node = this_node->next;
			continue;
		}
		// allocating w/i freed space
		if (this_node->size >= size) {
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
			return (this_node + 1);
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
	return (new_node + 1);
}
void my_free(void *ptr) {
	struct node *this_node = ptr - node_size;
	struct node *next_node = this_node->next;
	struct node *previous_node = this_node->previous;
	this_node->status = FREE;
	if (next_node != NULL && next_node->status == FREE) {
		this_node = coalesce(this_node, next_node);
	}
	if (previous_node != NULL && previous_node->status == FREE) {
		this_node = coalesce(previous_node, this_node);
	}
	ptr = this_node + 1;
	if ((this_node + 1 + (this_node->size/node_size)) == sbrk(0)) { 
		printf("sbrk(0) pre-dec %p\n", sbrk(0));
		printf("size+node %x\n", (this_node->size + node_size));
		sbrk(-(this_node->size + node_size));
		printf("decrement by size+node\n");
		printf("sbrk(0) post-dec %p\n", sbrk(0));
	} // decrement brk if freed space borders it
	if ((head->status == FREE) && head->next == NULL) {
		head = NULL;
	}
	printf("\n\n");
}
static struct node *coalesce(struct node *fst_node, struct node *snd_node) {
	fst_node->next = snd_node->next;
	if (snd_node->next != NULL) {
		snd_node->next->previous = fst_node;
	}
	fst_node->size = fst_node->size + snd_node->size + node_size;
	return fst_node;
}