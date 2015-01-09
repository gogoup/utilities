package org.gogoup.utilities.pagination;

public class PageOffset {
    
    private static final int MINIMAL_START = 1;

    private int start;
    private int size;
    
    public PageOffset() {
        this(1, 0);
    }
    
    public PageOffset(int start, int size) {
        setStart(start);
        setSize(size);
    }
    
    private void checkIfValidStart() {
        if (this.start < MINIMAL_START) {
            throw new IllegalArgumentException("Page start need to be larger than " + MINIMAL_START);
        }
    }
    
    private void checkIfValidSize() {
        if (this.size < 0) {
            throw new IllegalArgumentException("Page size need to be larger than or equal to 0.");
        }
    }

    public int getStart() {
        return start;
    }
    
    public int getSize() {
        return size;
    }
    
    private void setStart(int start) {
        this.start = start;
        checkIfValidStart();
    }

    private void setSize(int size) {
        this.size = size;
        checkIfValidSize();
    }

    public PageOffset startAtPage(int start) {
        setStart(start);
        return this;
    }
    
    public PageOffset withSize(int size) {
        setSize(size);
        return this;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + size;
        result = prime * result + start;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageOffset other = (PageOffset) obj;
        if (size != other.size)
            return false;
        if (start != other.start)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PageCursor [start=" + start + ", size=" + size + "]";
    }
    
}
