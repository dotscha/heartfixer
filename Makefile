
include mkdefs.select

JAVA_CP = .:c64
JAVA_SRC = \
	CTObject.java CodeRenderer.java FourPixels.java FourPixelsTok.java PixelPair16c.java \
	Vector.java Matrix.java Patterns.java Point.java Tables.java Triangle.java VRLMObject.java \
	threeaxrot.java c64/ReadCode.java c64/WriteCode.java c64/SpeedcodeUtils.java
JAVA_CLASSES = *.class */*.class

INCLUDES = 40x34irq.s 3axrot.s bitshader.s gprint.s pages.s stereog.s sphere.s screenplay.s \
	speedcode_tok1.s speedcode_tok2.s rot2.dat pattern.dat shadepat.dat

all : main_exo.prg
	ls -la *.prg
	$(EMU) $<

runexo : main_exo.prg
	$(EMU) main_exo.prg &

bas_exo.prg : prec_bas.prg
	$(EXO) sfx 16638 -t4 -o bas_exo.prg prec_bas.prg

main_exo.prg : main.prg
	$(EXO) sfx 10912 -t4 -o main_exo.prg main.prg

main.prg : main.s $(INCLUDES)
	$(CA) --cpu 6502X main.s
	$(LD) -t plus4 -o main.prg main.o

msx_test.prg : msx_test.s
	$(CA) msx_test.s
	$(LD) -t plus4 -o msx_test.prg msx_test.o
	$(EMU) msx_test.prg &

sp_tok1.exo : sp_tok1.bin
	$(EXO) raw -o sp_tok1.exo sp_tok1.bin

sp_tok1.bin : sp_tok1_bin.o
	$(LD) -t plus4 -o sp_tok1.bin sp_tok1_bin.o

sp_tok1_bin.o : sp_tok1_bin.s
	$(CA) sp_tok1_bin.s

sp: $(LIB)
	$(JAVA) -cp $(LIB) CodeRenderer -2x2tok model/MannequinHead.wrl > speedcode_tok1.s 2>speedcode_tok2.s
	tail -n 1 speedcode_tok1.s

rungp : gprint_test.prg
	$(EMU) gprint_test.prg &

gprint_test.prg : gprint_test.o
	$(LD) -t plus4 -o gprint_test.prg gprint_test.o

gprint_test.o : gprint_test.s gprint.s pages.s
	$(CA) gprint_test.s

stereog.s : $(LIB)
	$(JAVA) -cp $(LIB) Tables -stereog > stereog.s

sphere.s : $(LIB)
	$(JAVA) -cp $(LIB) Tables -sphere > sphere.s

pattern.dat : $(LIB)
	$(JAVA) -cp $(LIB) Patterns > pattern.dat

$(LIB) : $(JAVA_SRC)
	$(JAVAC) -cp $(JAVA_CP) $(JAVA_SRC)
	$(JAR) $(LIB) $(JAVA_CLASSES)

clean :
	@-$(RM) *.o main*.prg $(JAVA_SRC:.java=.class) $(LIB)

