package co.istad.inspectra.features.code;

import reactor.core.publisher.Flux;

public interface CodeService {

     Flux<Object> getComponentTree(String projectName,String page,String pageSize,String query);
    Flux<Object> getSubComponentTree(String projectName,String page,String pageSize, String query);


}
