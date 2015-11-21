warning("off");

image_files = glob("*.jpg");

for i = 1 : numel(image_files)
	printf("INSERT INTO Image_Descriptors VALUES (");
	printf("\"%s\", ", image_files{i});
	grid_imhist(imread(image_files{i}), 4, 3);
	printf(");\n");
endfor
