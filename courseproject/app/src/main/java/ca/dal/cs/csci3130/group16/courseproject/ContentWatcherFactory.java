package ca.dal.cs.csci3130.group16.courseproject;

//*
public class ContentWatcherFactory {
    private ContentWatcherFactory(){}
    public static <T> ContentWatcher make(Class<T> c, ContentWatcher.ContentListener<T> listener) {
        if (c == String.class) return new StringWatcher(listener);
        else if (c == Float.class) return new FloatWatcher(listener);
        else if (c == Integer.class) return new IntegerWatcher(listener);
        else throw new IllegalArgumentException("Unsupported Watcher Type");
    }

    private static class StringWatcher extends ContentWatcher {
        public StringWatcher(ContentListener listener) {
            super(listener);
        }
        @Override
        protected String convertContent(String content) {
            return content.trim();
        }
    }

    private static class FloatWatcher extends ContentWatcher {
        public FloatWatcher(ContentListener listener) {
            super(listener);
        }

        @Override
        protected Float convertContent(String content) {
            String salary = content.trim();
            if (!salary.isEmpty()) {
                return Float.parseFloat(salary);
            } else {
                return null;
            }
        }
    }

    private static class IntegerWatcher extends ContentWatcher {
        public IntegerWatcher(ContentListener listener) {
            super(listener);
        }

        @Override
        protected Integer convertContent(String content) {
            String duration = content.trim();
            if (!duration.isEmpty()) {
                return Integer.parseInt(duration);
            } else {
                return null;
            }
        }
    }
}
/**/