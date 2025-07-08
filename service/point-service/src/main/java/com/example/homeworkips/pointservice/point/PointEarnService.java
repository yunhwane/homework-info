package com.example.homeworkips.pointservice.point;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointEarnService implements PointEarnUseCase{

    private final SavePointPort savePointPort;

    @Override
    public PointEarnResult earn(PointEarnCommand pointEarnCommand) {
        return savePointPort.save(pointEarnCommand);
    }
}
