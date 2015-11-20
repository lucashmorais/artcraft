function [numElementsOnEachBin, binCenterVals] =  imhist(I, c, n)
	if (nargout == 0)
		imSize = size(I(:, :, 1));
		numElements = imSize(1) * imSize(2);
		reshapedImage = reshape(I(:, :, c), [1, numElements]);
		hist(reshapedImage, n)
	elseif (nargout == 1)
		imSize = size(I(:, :, 1));
		numElements = imSize(1) * imSize(2);
		reshapedImage = reshape(I(:, :, c), [1, numElements]);
		[numElementsOnEachBin, x] = hist(reshapedImage, n);
	elseif (nargout == 2)
		imSize = size(I(:, :, 1));
		numElements = imSize(1) * imSize(2);
		reshapedImage = reshape(I(:, :, c), [1, numElements]);
		[numElementsOnEachBin, binCenterVals] = hist(reshapedImage, n);
	else
		printf("Invalid number of output variables expected.\n");
	endif
endfunction
