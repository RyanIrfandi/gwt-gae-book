mkdir tmp
cd tmp
svn checkout http://gwt-gae-book.googlecode.com/svn/trunk/template/ template
tar cvfz ../template-1.0.tgz --exclude=".svn" template
cd ..
rm -rf tmp
