CC=g++
DEV=-g -Wall
OPT=-O2
 
ServerTest:
	$(CC) $(OPT) *.h  *.cpp -o servertest

clean: deltemp
	find -name "*.so" -exec rm {} \;
	find -name "*.o" -exec rm {} \;
	rm -f servertest

deltemp:
	find -name "*~" -exec rm {} \;
