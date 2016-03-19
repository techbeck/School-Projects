Rebecca Addison
rea36@pitt.edu

Notes:
	1. Each event holds 4 bytes, the x coordinate, the y coordinate, the radius (0 for bugs and pulses), and the type of event.
	2. Because the wave event x,y values are for the center of the wave, a bug checking the next position will not find it in the queue.
		To solve this, every time a wave event is processed, it checks both the next position and the position after that.

Circular queue of 512 bytes holds 128 events which are a word of data each
Pre-game loop until the b button is clicked:
	Get start time
	Initialize bug buster at bottom center
Main loop:
	Check for key press
	If right click
		Move right (wrap around screen)
	If left click
		Move left (wrap around screen)
	If up click
		Initialize pulse
		Insert pulse event into the queue
		Increment counter for pulses fired
	If down or b click
		Skip to end of game
	Check time
	If 2 minutes has passed since start time, skip to end of game
	If 100ms hasn't passed since last time check, restart main loop
	Generate bugs
		If first minute of game, generate 2 bugs every 1.5s
		If 30s after that, generate 3 bugs every 1s
		If last 30s of game, generate 4 bugs every 0.5s
	Get number of events in queue
	Animation loop for events in queue:
		Remove event from end of queue
		If bug event
			Turn off LED at current position
			If next position has a pulse
				Kill bug event
				Generate wave event
			Else
				Turn on LED at next position
		If pulse event
			Turn off LED at current position
			If next position has a bug
				Kill bug event
				Increment counter for bugs killed
				Generate wave event
			Else
				Turn on LED at next position
		If wave event
			If radius > 10
				Kill wave event
			Else
				Turn off LEDs at 8 positions around center
				If next position has a bug
					Kill bug event
					Increment counter for bugs killed
					Move wave event to new location and reset radius
				If position after that has a bug
					Kill bug event
					Increment counter for bugs killed
					Move wave event to new location and reset radius
				Else
					Turn on LEDs at next position
		Insert event at start of queue
		Turn on bug buster LED in case it got erased
	Get new time step value
Print out score message
End program
