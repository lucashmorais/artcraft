
public class PredictionInfo implements Comparable<PredictionInfo>{
	double hitRatio;
	double hitCount;
	double missCount;
	
	PredictionInfo(double newHitRatio, double newHitCount, double newMissCount)
	{
		hitRatio = newHitRatio;
		hitCount = newHitCount;
		missCount = newMissCount;
	}

	@Override
	public int compareTo(PredictionInfo o) {
		if (this.hitRatio > o.hitRatio)
			return -1;
		else if (this.hitRatio < o.hitRatio)
			return 1;
		else
			return (int) o.hitCount - (int) this.hitCount;
	}
}
