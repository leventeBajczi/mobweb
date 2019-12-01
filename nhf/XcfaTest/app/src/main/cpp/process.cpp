//
// Created by levente on 12/1/19.
//
#include "process.h"

void Process::addLabel(const std::string& label) {
    labelLut.insert(std::make_pair(label, new Label{}));
}

void Process::bindLabel(const std::string& label) {
    Label* toBind = labelLut.at(label);
    assembler.bind(toBind);
}

void Process::run() {
    assembler.run();
}

MacroAssembler *Process::getMasm() {
    return assembler.getMasm();
}

int64_t Process::getXReg(unsigned int i) {
    return assembler.getReg(i);
}

Label* Process::getLabel(const std::string& label) {
    return labelLut.at(label);
}

void Process::addGlobal(unsigned int reg, uint64_t value) {
    assembler.addGlobal(reg, value);
}
