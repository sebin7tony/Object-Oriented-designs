
// singleton
public class Find{

    FindInstance findInstance;

    public String run(String input){
        processInput(input);
        executeInstance();
        return findInstance.getResult();
    }

    private void processInput(String input){
        FindInstance findInstance = new FindInstance();
        String[] tokens = input.split(" ");
        //token[0] will be the command
        //token[1] will be the location
        findInstance.setFolderPath(tokens[1]);

        for(int i=2;i<tockens.length;i++){
            Filter filter = FilterFactory.getFilter(tokens[i]);
            if(filter.hasValue()){
                i++;
                filter.setValue(tokens[i]);
            }
            findInstance.addFilter(filter);
        }
    }

    private void executeInstance(){
        findInstance.applyFilters();
    }

}

public enum Filters{
    NAMEFILTER("-name")

    private String key;

    public Filters getFilter(String value){
        for(Filters filter : Filters.values()){
            if(filter.getValue().equals(value)){
                return filter;
            }
        }
    }

}

//factory for filters,singleton
public class FilterFactory{

    public static Filter getFilter(String filtername){
        Filter filter = null;;
        switch(filtername){
            case NAMEFILTER : 
                filter = new NameFilter();
                break;
            case TYPEFILTER :
                filter = new TypeFilter();
                break;
            default :
                break;
        }

        return filter;
    }
}

// class for storing the find command details
public class FindInstance {

    private String folderPath;
    private List<Filter> filters;
    private List<File> resultFiles;

    public void addFilter(Filter filter){
        this.filters.add(filter);
    }

    public void setFolderPath(String path){
        this.folderPath = path;
    }

    public String getResults(){
        return this.results;
    }

    private void applyFilters(){
        findFilePaths(folderPath,true);
        
        //apply the filters
        for(Filter filter : filters){
            resultFiles = filter.apply(resultFiles);
        }
    }

    //process the file system and find all the File/directories
    private void findFilePaths(String folderPath,boolean isRecursive)({

        File directory = new File(folderPath);
        List<File> curFileList = Arrays.asList(directory.listFiles());
        for(File file : curFileList){
            if(file.isDirectory()){
                if(isRecursive)
                    findFilePaths(file.getAbsolutePath(),isRecursive);
            }else{
                resultFiles.add(file);
            }
        }
    })
}

public interface Filter{

    public List<File> apply(List<File> files);

    public boolean hasValue();

    public void setValue();
}

public NameFilter implements Filter{

    private String nameValue;
    private List<File> result;

    public NameFilter(){
        result = new ArrayList<>();
    }

    @Override
    public List<File> apply(List<File> files){
        for(File file : files){
            if(file.getFileName().equals(this.nameValue)){
                result.add(file);
            }
        }

        return result;
    }
}

public TypeFilter implements Filter{

    private String type;
    private List<File> result;

    public TypeFilter(){
        this.type = type;
        result = new ArrayList<>();
    }

    @Override
    public List<File> apply(List<File> files){
        for(File file : files){
            if(!file.isDirectory()){
                result.add(file);
            }
        }

        return result;
    }
}