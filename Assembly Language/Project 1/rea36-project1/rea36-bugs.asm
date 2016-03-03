	.data
initT:	.space	4
q:	.space	512
	.text
	li	$v0, 30
	syscall
	sw	$a0, initT
	la	$k0, q+4
	la	$k0, q
loop:
	li	$v0, 30
	syscall
	lw	$t0, initT
	sub	$t1, $a0, $t0
	slti	$t1, $t1, 20000
	beq	$t1, $zero, endGame
	jal	generateBug
innerLoop:
	slti	$t0, $a1, 64
	beq	$t0, $zero, loop
	jal	bugEvent
	j 	innerLoop
endGame:
	li	$v0, 10
	syscall

	# generateBug()
	# generates bug event and inserts into queue
	# $a0 = x, set to random [0,63]
	# $a1 = y, set to 0
	# $a2 = r, set to 0
	# $a3 = type, set to b
generateBug:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	li	$v0, 42
	li	$a0, 0
	li	$a1, 64
	syscall
	move	$a0, $a0
	li	$a1, 0
	li	$a2, 3
	jal	_setLED
	li	$a2, 0
	li	$a3, 98
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra
	


	# bugEvent(x,y,0,b)
	# processes bug event
	# $a0 = x
	# $a1 = y, if not killed, $a1 set to $a1 + 1
	# $a2 = 0, no radius for bugs
	# $a3 = b, if killed, $a3 set to k
	# trashes: none
bugEvent:
	addi	$sp, $sp, -4
	sw	$ra, 0($sp)
	li	$a2, 0		# turn off LED
	jal	_setLED
	addi	$a1, $a1, 1
	li	$a2, 3		# turn on LED
	jal	_setLED
	li	$a2, 0
	lw	$ra, 0($sp)
	addi	$sp, $sp, 4
	jr	$ra

	# void _setLED(int x, int y, int color)
	#   sets the LED at (x,y) to color
	#   color: 0=off, 1=red, 2=yellow, 3=green
	#
	# arguments: $a0 is x, $a1 is y, $a2 is color
	# trashes:   $t0-$t3
	# returns:   none
	#
_setLED:
	# byte offset into display = y * 16 bytes + (x / 4)
	sll	$t0,$a1,4      # y * 16 bytes
	srl	$t1,$a0,2      # x / 4
	add	$t0,$t0,$t1    # byte offset into display
	li	$t2,0xffff0008 # base address of LED display
	add	$t0,$t2,$t0    # address of byte with the LED
	# now, compute led position in the byte and the mask for it
	andi	$t1,$a0,0x3    # remainder is led position in byte
	neg	$t1,$t1        # negate position for subtraction
	addi	$t1,$t1,3      # bit positions in reverse order
	sll	$t1,$t1,1      # led is 2 bits
	# compute two masks: one to clear field, one to set new color
	li	$t2,3		
	sllv	$t2,$t2,$t1
	not	$t2,$t2        # bit mask for clearing current color
	sllv	$t1,$a2,$t1    # bit mask for setting color
	# get current LED value, set the new field, store it back to LED
	lbu	$t3,0($t0)     # read current LED value	
	and	$t3,$t3,$t2    # clear the field for the color
	or	$t3,$t3,$t1    # set color field
	sb	$t3,0($t0)     # update display
	jr	$ra
	
	# int _getLED(int x, int y)
	#   returns the value of the LED at position (x,y)
	#
	#  arguments: $a0 holds x, $a1 holds y
	#  trashes:   $t0-$t2
	#  returns:   $v0 holds the value of the LED (0, 1, 2 or 3)
	#
_getLED:
	# byte offset into display = y * 16 bytes + (x / 4)
	sll  $t0,$a1,4      # y * 16 bytes
	srl  $t1,$a0,2      # x / 4
	add  $t0,$t0,$t1    # byte offset into display
	la   $t2,0xffff0008
	add  $t0,$t2,$t0    # address of byte with the LED
	# now, compute bit position in the byte and the mask for it
	andi $t1,$a0,0x3    # remainder is bit position in byte
	neg  $t1,$t1        # negate position for subtraction
	addi $t1,$t1,3      # bit positions in reverse order
    	sll  $t1,$t1,1      # led is 2 bits
	# load LED value, get the desired bit in the loaded byte
	lbu  $t2,0($t0)
	srlv $t2,$t2,$t1    # shift LED value to lsb position
	andi $v0,$t2,0x3    # mask off any remaining upper bits
	jr   $ra
