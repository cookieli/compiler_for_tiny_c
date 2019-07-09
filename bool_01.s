.file "tests/codeGenOfMe/bool_01.dcf"
.text
.section .rodata
.LC0:
.string "correct\n"
.LC1:
.string "incorrect\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movb $1,-4(%rbp)
movb -4(%rbp),%al
movb %al,-3(%rbp)
movb -3(%rbp),%al
cmpb $0,%al
jle .L3
movb $0,-2(%rbp)
jmp .L4
.L3:
movb $1,-2(%rbp)
.L4:
movb -4(%rbp),%al
movb %al,-1(%rbp)
movb -2(%rbp),%al
movb -1(%rbp),%r10b
cmpb %r10b,%al
je .L5
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L5:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

