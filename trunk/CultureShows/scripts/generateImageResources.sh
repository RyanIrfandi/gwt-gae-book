# given a folder, generate this code to be used in a ClientBundle or ui.xml
folder=$1;
for i in $(ls $folder)
do
	base=${i%.*}

	#echo "@Source(\"$folder/$i\")"
	#echo "ImageResource $base();"
	echo "<ui:image field=\"$base\" src=\"$folder/$i\" />"
	echo "<g:Image resource=\"{$base}\" />"

	echo ""
done
