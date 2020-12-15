package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.controller.ObjectiveController;
import capstone.backend.api.dto.CreateCfrDto;
import capstone.backend.api.entity.*;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.CfrService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CfrServiceImpl implements CfrService {

    private final CfrRepository cfrRepository;

    private final ObjectiveRepository objectiveRepository;

    private final UserRepository userRepository;

    private final ReportRepository reportRepository;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveController.class);

    private final CommonProperties commonProperties;

    private final EvaluationCriteriaRepository criteriaRepository;

    private final CycleRepository cycleRepository;

    private final JwtUtils jwtUtils;

    private final CommonUtils commonUtils;

    @Override
    public ResponseEntity<?> getListWaiting(int page, int limit, String token) throws Exception {
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> superior = new HashMap<>();
        Map<String,Object> inferior = new HashMap<>();

        if(page <= 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID())
                            .build()
            );
        }
        if(limit == 0){
            limit = 10;
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        Page<Report> superiors = reportRepository.findAllInferiorRequest(user.getId(), PageRequest.of(page-1,limit));
        List<Map<String,Object>> items = setListCheckin(superiors);

        Map<String,Object> checkins = new HashMap<>();
        checkins.put("items",items);
        checkins.put("meta",commonUtils.paging(superiors,page));

        superior.put("type","MEMBER_TO_LEADER");
        superior.put("checkins",checkins);

        Page<Report> inferiors = reportRepository.findAllSuperiorRequest(user.getId(), PageRequest.of(page-1,limit));

        List<Map<String,Object>> item1s = setListCheckin(inferiors);

        Map<String,Object> checkin1s = new HashMap<>();
        checkin1s.put("items",item1s);
        checkin1s.put("meta",commonUtils.paging(inferiors,page));

        inferior.put("type","LEADER_TO_MEMBER");
        inferior.put("checkins",checkin1s);

        response.put("superior",superior);
        response.put("inferior",inferior);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getHistoryCfrs(int page, int limit, long cycleId,int type, String token) throws Exception {
        Map<String,Object> response = new HashMap<>();
        List<Map<String,Object>> items = new ArrayList<>();
        Page<Cfr> pages;
        if(page <= 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID())
                            .build()
            );
        }
        if(limit == 0){
            limit = 10;
        }
        if(type <1 || type > 3){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID())
                            .build()
            );
        }

        Cycle cycle = cycleRepository.findById(cycleId).orElse(null);
        if (cycle== null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Cfr> cfrs = cfrRepository.findAllByCycleId(cycleId);
                  cfrs.addAll(cfrRepository.findAllByCycleId2(cycleId));
        List<Cfr> tempCfrs;
        if(type == 1){
            tempCfrs = cfrs.stream().filter(cfr -> cfr.getSender().getId() == user.getId()).collect(Collectors.toList());
        } else if(type == 2){
            tempCfrs = cfrs.stream().filter(cfr -> cfr.getReceiver().getId() == user.getId()).collect(Collectors.toList());
        } else{
            tempCfrs = cfrs;
        }
        pages = new PageImpl<>(tempCfrs,PageRequest.of(page-1,limit),tempCfrs.size());

        pages.getContent().forEach(cfr ->{
            Map<String,Object> item = new HashMap<>();
            Map<String,Object> eva = new HashMap<>();
            Map<String,Object> sender = new HashMap<>();
            Map<String,Object> receiver = new HashMap<>();
            Map<String,Object> objective = new HashMap<>();
            Map<String,Object> checkin = new HashMap<>();

            EvaluationCriteria eval = cfr.getEvaluationCriteria();
            eva.put("id",eval.getId());
            eva.put("content",eval.getContent());
            eva.put("numberOfStar",eval.getNumberOfStar());
            eva.put("type",eval.getType());

            User send = cfr.getSender();
            sender.put("id",send.getId());
            sender.put("fullName",send.getFullName());
            sender.put("avatarUrl",send.getAvatarImage());

            User receive = cfr.getReceiver();
            receiver.put("id",receive.getId());
            receiver.put("fullName",receive.getFullName());
            receiver.put("avatarUrl",receive.getAvatarImage());


            if(cfr.getType().equalsIgnoreCase("RECOGNITION")){
                Objective obj = cfr.getObjective();
                objective.put("id",obj.getId());
                objective.put("title",obj.getName());

                item.put("objective",objective);
                item.put("checkin",null);
            }else{
                Report report = cfr.getReport();
                Objective obj = report.getObjective();
                objective.put("id",obj.getId());
                objective.put("title",obj.getName());

                checkin.put("id",report.getId());
                checkin.put("objective",objective);

                item.put("checkin",checkin);
                item.put("objective",null);

            }

            item.put("id",cfr.getId());
            item.put("content",cfr.getContent());
            item.put("type",cfr.getType().equalsIgnoreCase("RECOGNITION") ? "recognition" : "feedback");
            item.put("evaluationCriteria",eva);
            item.put("sender",sender);
            item.put("receiver",receiver);
            item.put("createAt",cfr.getCreateAt());

            items.add(item);
        });

        response.put("items",items);
        response.put("meta",commonUtils.paging(pages,page));

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getUserStar(long cycleId) throws Exception {
        List<Map<String,Object>> responses = new ArrayList<>();

        List<User> users;
        if(cycleId == 0){
            users = userRepository.findRankingStar();
        } else {
            List<Cfr> cfrs = cfrRepository.findAllByCycleId(cycleId);
                      cfrs.addAll(cfrRepository.findAllByCycleId2(cycleId));
            users = new ArrayList<>();
            for (Cfr cfr : cfrs) {
                boolean isIn = false;
                for (User user : users) {
                    if(user.getId() == cfr.getReceiver().getId()){
                        user.setStar(user.getStar() + cfr.getEvaluationCriteria().getNumberOfStar());
                        isIn = true;
                    }
                }
                if(!isIn){
                    User user = userRepository.findByEmail(cfr.getReceiver().getEmail()).get();
                    user.setStar(user.getStar() + cfr.getEvaluationCriteria().getNumberOfStar());
                    users.add(user);
                }

            }
            sortArray(users);
        }

        users.forEach(user -> {
            Map<String,Object> u = new HashMap<>();
            u.put("user_fullName",user.getFullName());
            u.put("avatarUrl",user.getAvatarImage());
            u.put("user_id",user.getId());
            u.put("department",user.getDepartment().getName());
            u.put("sum",user.getStar());

            responses.add(u);
        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getDetailCfr(long id) throws Exception {
        Map<String,Object> response = new HashMap<>();
        Cfr cfr = cfrRepository.findById(id).get();

        Map<String,Object> eva = new HashMap<>();
        Map<String,Object> sender = new HashMap<>();
        Map<String,Object> receiver = new HashMap<>();
        Map<String,Object> objective = new HashMap<>();
        Map<String,Object> checkin = new HashMap<>();

        eva.put("content",cfr.getEvaluationCriteria().getContent());
        sender.put("fullName",cfr.getSender().getFullName());
        receiver.put("fullName",cfr.getReceiver().getFullName());

        if(cfr.getType().equalsIgnoreCase("RECOGNITION")){
            Objective obj = cfr.getObjective();
            objective.put("title",obj.getName());

            response.put("objective",objective);
            response.put("checkin",null);
        }else{
            Report report = cfr.getReport();
            Objective obj = report.getObjective();
            objective.put("title",obj.getName());

            checkin.put("checkinAt",report.getCheckinDate());
            checkin.put("objective",objective);

            response.put("checkin",checkin);
            response.put("objective",null);

        }

        response.put("id",cfr.getId());
        response.put("content",cfr.getContent());
        response.put("type",cfr.getType().equalsIgnoreCase("RECOGNITION") ? "recognition" : "feedback");
        response.put("evaluationCriteria",eva);
        response.put("sender",sender);
        response.put("receiver",receiver);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> createCfr(CreateCfrDto dto,String token) throws Exception {

        EvaluationCriteria eva = criteriaRepository.findById(dto.getEvaluationCriteriaId()).get();

        User receiver = userRepository.findById(dto.getReceiverId()).get();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User sender = userRepository.findByEmail(email).get();

        Objective objective = null;
        Report report = null;

        if(dto.getObjectiveId() != 0){
            objective = objectiveRepository.findById(dto.getObjectiveId()).orElse(null);
        }
        if(dto.getCheckinId() != 0) {
            report = reportRepository.findById(dto.getCheckinId()).orElse(null);

        }
        if((objective == null && eva.getType().equalsIgnoreCase("recognition"))
            || (report == null && !eva.getType().equalsIgnoreCase("recognition"))){
            return ResponseEntity.status(commonProperties.getHTTP_FAILED()).body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message("Nếu type là recognition thì objective ko được null, nếu type khác recognition thì report ko được null")
                            .build()
            );
        }

        if(report != null){
            if (eva.getType().equalsIgnoreCase("leader_to_member")) {
                report.setLeaderFeedback(true);
            } else {
                report.setStaffFeedback(true);
            }
            reportRepository.save(report);
        }

        Cfr cfr = Cfr.builder()
                .content(dto.getContent())
                .createAt(new Date())
                .evaluationCriteria(eva)
                .objective(objective)
                .receiver(receiver)
                .sender(sender)
                .report(report)
                .type(eva.getType())
                .build();

        cfrRepository.save(cfr);

        receiver.setStar(receiver.getStar() + eva.getNumberOfStar());
        userRepository.save(receiver);

        return ResponseEntity.status(commonProperties.getCODE_SUCCESS()).body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    private List<Map<String,Object>> setListCheckin(Page<Report> reports){
        List<Map<String,Object>> items = new ArrayList<>();
        reports.getContent().forEach(report -> {
            //get information of reviewer
            User reviewer = report.getAuthorizedUser();
            // get information object
            Objective objective = report.getObjective();
            // get information of author
            User author = objective.getExecute().getUser();

            // get information reviewer
            Map<String,Object> item = new HashMap<>();

            Map<String,Object> review = new HashMap<>();
            review.put("id",reviewer.getId());
            review.put("fullName",reviewer.getFullName());
            review.put("avatarUrl",reviewer.getAvatarImage());

            // get information user create object
            Map<String,Object> authorUser = new HashMap<>();
            authorUser.put("id",author.getId());
            authorUser.put("fullName",author.getFullName());
            authorUser.put("avatarUrl",author.getAvatarImage());

            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("id",objective.getId());
            objectMap.put("title",objective.getName());
            objectMap.put("user",authorUser);

            item.put("id",report.getId());
            item.put("checkinAt",report.getCheckinDate());
            item.put("reviewer",review);
            item.put("objective",objectMap);

            items.add(item);

        });

        return items;
    }

    private void sortArray(List<User> users){
        for (int i = 0; i < users.size()-1; i++) {
            for (int i1 = i+1; i1 < users.size(); i1++) {
                if(users.get(i).getStar() < users.get(i1).getStar()){
                    User temp = users.get(i);
                    users.set(i,users.get(i1));
                    users.set(i1,temp);
                }
            }
        }
    }
}
