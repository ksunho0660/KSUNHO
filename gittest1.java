@Override
void build(final Controller controller, final Movie parent, final Layer layer, final Sequence sequence) {
	this.controller = controller;
	this.parent = parent;
	this.layer = layer;
	this.sequence = sequence;
	// frames = sequence.getFrames();
	final int rate = parent.getFrameRate();
	try {
		final Mixer mixer = AudioSystem.getMixer(null);
		final AudioFileFormat file = AudioSystem.getAudioFileFormat(url);
		final AudioInputStream stream = AudioSystem.getAudioInputStream(url);
		final AudioFormat format = stream.getFormat();
		int size = (int) Math.rint((format.getFrameRate() * format.getFrameSize()) / rate);
		size = (size + format.getFrameSize()) - (size % format.getFrameSize());
		// size = size - size % format.getFrameSize();
		size = Math.min(size, file.getByteLength());
		byte[] buffer;
		while (stream.read(buffer = new byte[size], 0, size) > -1) {
			queue.addLast(buffer);
		}
		line = (SourceDataLine) mixer.getLine(new DataLine.Info(SourceDataLine.class, format));
		line.open();
	}
	catch (final Exception e) {
		e.printStackTrace();
		line = null;
	}
}