package seeya.insight.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Component
public class CommonMapper {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 목록 : 페이지 처리없는 목록. 조건 없음
     * @param query
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List list(String query){
        return sqlSessionTemplate.selectList(query);
    }

    /**
     * 목록 : 페이지 처리없는 목록. 조건 포함
     * @param queryId
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List list(String queryId, Object params){
        return sqlSessionTemplate.selectList(queryId,params);
    }

    /**
     * ㅜ가
     * @param queryId
     * @param params
     * @return
     */
    public Object insert(String queryId, Object params){
        return sqlSessionTemplate.insert(queryId, params);
    }

    /**
     * 수정
     * @param queryId
     * @param params
     * @return
     */
    public Object update(String queryId, Object params){
        return sqlSessionTemplate.update(queryId, params);
    }

    /**
     * 삭제
     * @param queryId
     * @param params
     * @return
     */
    public Object delete(String queryId, Object params){
        return sqlSessionTemplate.delete(queryId, params);
    }

    /**
     * 1개의 데이터 가져오기 : 조건 없음
     * @param queryId
     * @return
     */
    public Object selectOne(String queryId){
        return sqlSessionTemplate.selectOne(queryId);
    }

    /**
     * 1개의 데이터 가져오기 : 조건 포함
     * @param queryId
     * @param params
     * @return
     */
    public Object selectOne(String queryId, Object params){
        return sqlSessionTemplate.selectOne(queryId, params);
    }

    /**
     * 목록 : 페이지 처리
     * @param queryId
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object selectPagingList(String queryId, Object params){
        Map<String, Object> map = (Map<String, Object>) params;

        String strPageIndex = (String) map.get("page");         // 현재 페이지 번호
        String strPageRow   = (String) map.get("listCount");    // 한페이지에 보여줄 행의 수
        int nPageIndex = 0;     // 현재 페이지 기본값
        int nPageRow   = 15;    // 한 페이지에 보여줄 행의 수 기본값

        // 현재 페이지번호가 있으면 페이지 시작의 기본값은 현재 페이지 번호 - 1이 된다.
        if (StringUtils.isEmpty(strPageIndex) == false) {
            nPageIndex = Integer.parseInt(strPageIndex) - 1;
        }

        // 한 페이지에서 보여줄 행의 수가 있으면 행의 수 기본값을 변경한다.
        if (StringUtils.isEmpty(strPageRow) == false) {
            nPageRow = Integer.parseInt(strPageRow);
        }

//        map.put("START", (nPageIndex * nPageRow) + 1);          // 페이지 시작 = (현재 페이지 * 행의 수) - 1
        map.put("START", (nPageIndex * nPageRow));          // 페이지 시작 = (현재 페이지 * 행의 수)
        map.put("END"  , (nPageIndex * nPageRow) + nPageRow);   // 페이지 끝   = (현재 페이지 * 행의 수) + 행의 수

        return sqlSessionTemplate.selectList(queryId, map);
    }

    /**
     * 목록 팝업용 : 페이징 처리
     * @param queryId
     * @param params
     * @return
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public Object selectPagingListPopup(String queryId, Object params){
        Map<String, Object> map = (Map<String, Object>) params;

        String strPageIndex = (String) map.get("ppage");          // 현재 페이지 번호
        String strPageRow   = (String) map.get("plistCount");     // 한페이지에 보여줄 행의 수
        int nPageIndex = 0;
        int nPageRow   = 15;

        if (StringUtils.isEmpty(strPageIndex) == false) {
            nPageIndex = Integer.parseInt(strPageIndex) - 1;
        }

        if (StringUtils.isEmpty(strPageRow) == false) {
            nPageRow = Integer.parseInt(strPageRow);
        }

        map.put("START", (nPageIndex * nPageRow) + 1);
        map.put("END", (nPageIndex * nPageRow) + nPageRow);

        return sqlSessionTemplate.selectList(queryId, map);
    }

}
