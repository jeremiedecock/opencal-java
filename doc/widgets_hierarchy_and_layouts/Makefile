DOT = dot -Tpng

.PHONY : all clean

all : ExplorerTab.png MainWindow.png MakerTab.png ReviewerTab.png StatsTab.png

ExplorerTab.png : ExplorerTab.dot
	$(DOT) $< -o $@

MainWindow.png : MainWindow.dot
	$(DOT) $< -o $@

MakerTab.png : MakerTab.dot
	$(DOT) $< -o $@

ReviewerTab.png : ReviewerTab.dot
	$(DOT) $< -o $@

StatsTab.png : StatsTab.dot
	$(DOT) $< -o $@

clean :
	rm *.png
