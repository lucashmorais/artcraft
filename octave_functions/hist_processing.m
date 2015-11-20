warning("off");

image_files = glob("*.jpg");

for i = 1 : numel(image_files)
	printf("%s, ", image_files{i});
	grid_imhist(imread(image_files{i}), 4, 3);
endfor
