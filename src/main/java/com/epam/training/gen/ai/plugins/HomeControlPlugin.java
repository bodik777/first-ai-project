package com.epam.training.gen.ai.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class HomeControlPlugin {

    private final Map<String, Lamp> lamps = Map.of("corridor", new Lamp(1, false, "corridor"),
            "living room", new Lamp(2, false, "living room"),
            "bathroom", new Lamp(3, false, "bathroom"));

    @DefineKernelFunction(name = "turnOffLamp", description = "turn off the lamp by name")
    public Lamp turnOffLamp(
            @KernelFunctionParameter(name = "name") String lampName) {
        log.info("Turning off lamp with name: [{}]", lampName);
        Lamp lamp = lamps.get(lampName.toLowerCase());
        if (lamp != null) {
            lamp.isEnabled = false;
            log.info("Turned off lamp: [{}]", lamp);
        } else {
            throw new IllegalArgumentException("Lamp not found");
        }
        return lamp;
    }

    @DefineKernelFunction(name = "turnOnLamp", description = "turn on the lamp by name")
    public Lamp turnOnLamp(
            @KernelFunctionParameter(name = "lampName") String lampName) {
        log.info("Turning on lamp with name: [{}]", lampName);
        Lamp lamp = lamps.get(lampName.toLowerCase());
        if (lamp != null) {
            lamp.isEnabled = true;
            log.info("Turned on lamp: [{}]", lamp);
        } else {
            throw new IllegalArgumentException("Lamp not found");
        }
        return lamp;
    }

    @DefineKernelFunction(name = "getHomeLamps", description = "get the list of lamps at home")
    public String getHomeLamps() {
        log.info("Returned all lamps : [{}]", lamps);
        return lamps.values().toString();
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    public class Lamp {
        Integer id;
        boolean isEnabled;
        String name;
    }

}
