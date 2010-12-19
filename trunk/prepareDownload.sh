mkdir tmp
cd tmp
svn checkout http://gwt-gae-book.googlecode.com/svn/trunk/CultureShows/ CultureShows
tar cvfz ../CultureShows-1.0.tgz --exclude=".svn" CultureShows 
cd ..
rm -rf tmp


