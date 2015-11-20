function bin_vals = grid_imhist (I, n, M)
	im_size = size(I(:, :, 1));

	x_size = im_size(1);
	y_size = im_size(2);

	int64(x_delta = x_size / M);
	int64(y_delta = y_size / M);
	bin_vals = [];
	
	for i = 0 : M - 2
		for j = 0 : M - 2
			for c = 1 : size(I, 3)
				%printf("%d, %d, %d\n", c, i, j);
				bin_vals = [bin_vals, imhist(I(x_delta * i + 1 : x_delta * (i + 1), y_delta * j + 1 : y_delta * (j + 1), :), c, n)];	
			endfor
		endfor
	endfor

	i = M - 1;
	for j = 0 : M - 2
		for c = 1 : size(I, 3)
			bin_vals = [bin_vals, imhist(I(x_delta * i + 1 : x_size, y_delta * j + 1 : y_delta * (j + 1), :), c, n)];	
		endfor
	endfor
	
	j = M - 1;
	for i = 0 : M - 2
		for c = 1 : size(I, 3)
			bin_vals = [bin_vals, imhist(I(x_delta * i + 1 : x_delta * (i + 1), y_delta * j + 1 : y_size, :), c, n)];	
		endfor
	endfor

	i = M - 1;
	j = M - 1;
	for c = 1 : size(I, 3)
		bin_vals = [bin_vals, imhist(I(x_delta * i + 1 : x_size, y_delta * j + 1 : y_size, :), c, n)];	
	endfor
	
	if (nargout == 0)
		for i = 1 : numel(bin_vals) - 1
			printf("%d, ", bin_vals(i));
		endfor
		printf("%d\n", bin_vals(numel(bin_vals)));
	endif
endfunction
